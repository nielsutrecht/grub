import { Component } from '@angular/core';

import { User } from './user';
import { UserService, LoginResponse } from './user.service';

@Component({
  selector: 'user-login',
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})

export class UserLoginComponent {
  constructor(
    private userService: UserService,
  ) {}

  login(): void {
    console.info(`email: ${this.email}, password: ${this.password}`);
    this.userService.login(this.email, this.password)
      .then(response => {
        this.response = response;
        this.get();
      });
  }

  get(): void {
    this.userService.get()
        .then(user => {
          console.info(user);
        });
  }

  user: User;
  email;
  password;
  response: LoginResponse;
}
