import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service'
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  title = 'Dashboard';
  greeting = {};

  constructor(private user: UserService, private http: HttpClient) {

  }
  ngOnInit() {
    this.http.get('/api/resource').subscribe(
      data => console.log(data),
      error => console.log(error),
      () => console.log('completed!'));
  }





  authenticated() { return this.user.authenticated; }

}
