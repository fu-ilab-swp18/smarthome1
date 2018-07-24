import { Component, OnInit } from '@angular/core';
import { UserService } from '..//user.service';
import { Sensor } from '..//sensor';
//import 'rxjs/add/operator/map';
import { map } from 'rxjs/operators';
import {Observable} from 'rxjs/Observable';
import { SensorEvent } from '..//sensorevent';




@Component({
  selector: 'app-sensors',
  templateUrl: './sensors.component.html',
  styleUrls: ['./sensors.component.css']
})
export class SensorsComponent implements OnInit {

  sensorEvents: SensorEvent[];
  eventsO : Observable<SensorEvent[]>;


  constructor(private userService:UserService) { 
    this.sensorEvents = [];
  }

  ngOnInit() {
    this.sensorEventQuery();
  }

  sensorEventQuery() {
     this.userService.sensorEventQuery()
    .subscribe(data => {
      this.sensorEvents = data;
      console.log(this.sensorEvents[0])
   });

  }


  sensorEventQuery2() {
    this.eventsO = this.userService.sensorEventQuery();
  }

}
