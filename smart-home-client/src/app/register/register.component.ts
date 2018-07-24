import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import { User } from '../user';
import { Router } from '@angular/router';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  user : User = new User();

  constructor(private router:Router, private userService:UserService) { }

  ngOnInit() {
  }

  register() {

    console.log("Register new User : ")
    console.log(this.user);

    this.userService.createAccount(this.user,
      () => {
        console.log("Account created!!");
        this.router.navigate(['login']); 
      });

    return false;

    /*
    if (username == 'admin' && password == 'admin'){
      this.router.navigate(['dashboard']);
    } */
  }
}
