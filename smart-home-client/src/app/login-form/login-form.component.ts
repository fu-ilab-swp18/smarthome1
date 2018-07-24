import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '..//user.service';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent implements OnInit {

  credentials = {username: '', password: ''};

  constructor(private router: Router, private userService: UserService) { }

  ngOnInit() {
  }

  loginUser() {

    this.userService.authenticate(this.credentials, () => {
      this.router.navigate(['post']);
    });

    return false;

    /*
    if (username == 'admin' && password == 'admin'){
      this.router.navigate(['dashboard']);
    }*/
  }

}
