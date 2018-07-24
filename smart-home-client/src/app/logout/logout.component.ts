import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import { Router } from '@angular/router';



@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent implements OnInit {

  constructor(private userService:UserService, private router: Router) { 
  }

  ngOnInit() {
    console.log("Logout");
    this.userService.logout();
    this.userService.authenticated = false;
    this.router.navigate(['login']);
  }

}
