import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { User } from './user';
import 'rxjs/add/operator/map'
import {Observable} from 'rxjs/Observable';
import { Sensor } from './sensor';
import { SensorEvent } from './sensorevent';
import { AlcSensor } from './alcSensor';
import { AlcSensorEvent } from './alcSensorEvent';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  authenticated = false;

  constructor(private http: HttpClient) {
  }

  createAccount(user:User, callback){
    console.log("This is the Account Creation Service!")
    this.http.post('/api/register',user)
      .subscribe(
        resp=> {
          console.log(resp)
        },
        error => {
          console.log(error);
        },
        () => {
          callback();
        });

    //return callback && callback();
  }


  authenticate(credentials, callback) {
        const headers = new HttpHeaders(credentials ? {
            authorization : 'Basic ' + btoa(credentials.username + ':' +
              credentials.password)
        } : {});

        this.http.get('api/user', {headers: headers}).subscribe(response => {
            if (response['name']) {
                console.log("Successfully logged in!!")
                this.authenticated = true;
            } else {
                this.authenticated = false;
            }
            return callback && callback();
        });
    }

    logout() {
      console.log("Log him out")
      this.http.post('api/logout', {}).subscribe(resp=>console.log(resp));
    }

    sensorEventQuery(): Observable<SensorEvent[]>{
      
      /* ToDo: Set sensor  */
      let sensor = new Sensor(1);

      return this.http.post<SensorEvent[]>('api/getsensorevents', sensor);
    }

    alcSensorEventQuery(): Observable<AlcSensorEvent[]>{
      let alkohol = new AlcSensor(1);

      return this.http.post<AlcSensorEvent[]>('api/getalcsensorevents', alkohol);
    }

    getConfig() {
      // now returns an Observable of Config
      //return this.http.get<List<Sensor>('api/usersensors');


    }

  }
