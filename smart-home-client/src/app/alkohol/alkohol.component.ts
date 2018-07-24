import { Component, OnInit } from '@angular/core';
import { UserService } from '..//user.service';
import { AlcSensor } from '..//alcSensor';
//import 'rxjs/add/operator/map';
import { map } from 'rxjs/operators';
import {Observable} from 'rxjs/Observable';
import { AlcSensorEvent } from '..//alcSensorEvent';

@Component({
  selector: 'app-alkohol',
  templateUrl: './alkohol.component.html',
  styleUrls: ['./alkohol.component.css']
})
export class AlkoholComponent implements OnInit {

  alcSensorEvents: AlcSensorEvent[];
  eventsO : Observable<AlcSensorEvent[]>;


  constructor(private userService:UserService) { 
    this.alcSensorEvents = [];
  }

  ngOnInit() {
    this.alcSensorEventQuery();
  }

  alcSensorEventQuery() {
     this.userService.alcSensorEventQuery()
    .subscribe(data => {
      this.alcSensorEvents = data;
      console.log(this.alcSensorEvents[0])
   });

  }


  alkoholEventQuery2() {
    this.eventsO = this.userService.alcSensorEventQuery();
  }

}
