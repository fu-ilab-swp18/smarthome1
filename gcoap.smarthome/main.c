/*
 * Copyright (c) 2015-2016 Ken Bannister. All rights reserved.
 *
 * This file is subject to the terms and conditions of the GNU Lesser
 * General Public License v2.1. See the file LICENSE in the top level
 * directory for more details.
 */

/**
 * @ingroup     examples
 * @{
 *
 * @file
 * @brief       gcoap example
 *
 * @author      Ken Bannister <kb2ma@runbox.com>
 *
 * @}
 */

#include <stdio.h>
#include "msg.h"

#include "net/gcoap.h"
#include "kernel_types.h"
#include "shell.h"
#include "mma8x5x.h"
#include "mma8x5x_params.h"
#include "xtimer.h"
#include "periph/gpio.h"
#include "mq3.h"

#include "periph_conf.h"
#include "board.h"

#define MAIN_QUEUE_SIZE (4)
#define SMARTHOME_TYPE_MAILBOX (1)
#define SMARTHOME_TYPE_ALC (2)
#define MAIN_QUEUE_SIZE (4)

static msg_t _main_msg_queue[MAIN_QUEUE_SIZE];

extern int gcoap_cli_cmd(int argc, char **argv);
extern void gcoap_cli_init(void);
extern void gcoap_snd_msg(char *payload);
int smarthome_cmd(int argc, char **argv);
int alc_cmd(int argc, char **argv);
int set_sensor_cmd(int argc, char **argv);



static const shell_command_t shell_commands[] = {
    { "coap", "CoAP example", gcoap_cli_cmd },
    { "smarthome", "Smarthome example", smarthome_cmd},
    { "alc", "Alc example", alc_cmd},
    { "set_sid", "Set sensor id", set_sensor_cmd},
    { NULL, NULL, NULL }
};

int counter = 1;
int sid;
bool closed = true;
bool sober = true;



static void cb(float y)
{
#define bufsize (16)

    if (y < 500 && closed){
        char payload[bufsize];
        sprintf(payload, "%d,%d", sid, SMARTHOME_TYPE_MAILBOX);  
        printf("You Received A Mail // Number : %d \n", counter++);
        gcoap_snd_msg(payload);
        closed = false;
    } 

    if (y > 500 && !closed){
        closed = true;
    }
}

static void cb_alc(int conc){

    if (conc > 500 && sober){            
        char payload[bufsize];
        sprintf(payload, "%d,%d", sid, SMARTHOME_TYPE_ALC);  
        printf("Alcohol Event!! \n");
        gcoap_snd_msg(payload);
        sober = false;
    } 

    if (conc < 500 && !sober){
        sober = true;
    }
}


static void but_cb(void *arg){
#define bufsize (16)
    printf("yout emptied your mailbox : %d\n", (int)arg);

    char payload[bufsize];
    sprintf(payload, "%s", "empty");   // here 2 means binary
    gcoap_snd_msg(payload);
}


int main(void)
{
    /* for the thread running the shell */
    msg_init_queue(_main_msg_queue, MAIN_QUEUE_SIZE);
    gcoap_cli_init();
    puts("gcoap example app");

    /* start shell */
    puts("All up, running the shell now");


    int cnt = 0;
    if (gpio_init_int(BTN0_PIN, BTN0_MODE, GPIO_FALLING, but_cb, (void *)cnt) < 0) {
        puts("[FAILED] init BTN0!");
    }


    char line_buf[SHELL_DEFAULT_BUFSIZE];
    shell_run(shell_commands, line_buf, SHELL_DEFAULT_BUFSIZE);

    /* should never be reached */
    return 0;
}



int smarthome_cmd(int argc, char **argv){
    mma8x5x_t dev;
    mma8x5x_data_t data;

    printf("Execute smarthome command!!%d, %s\n", argc, argv[0]);

    mma8x5x_init(&dev, mma8x5x_params);
    
    while(1){
        //printf("Hallo");
        mma8x5x_read(&dev, &data);
        printf("Acceleration [in mg]: X: %d, Y: %d, Z: %d\n", data.x, data.y, data.z);

        xtimer_usleep(1000*100);
        
        cb(data.y);
    }
    return 0;
}


int alc_cmd(int argc, char **argv){
#define bufsize (16)

    printf("Execute smarthome command!!%d, %s\n", argc, argv[0]);

    mq3_t dev;
    adc_t line = 0;

    mq3_init(&dev, line);

    while(1){
        int conc = mq3_read_raw(&dev);
	printf("Current Alc Concentration : %di\n", conc);
        cb_alc(conc);
        xtimer_usleep(1000*100);
    }

}

int set_sensor_cmd(int argc, char **argv){
    if (argc == 2){
        sid = atoi(argv[1]);
        printf("New Sensor ID : %d\n", sid);
    }
    return 0;
}

