import { Component } from '@angular/core';
import {RegistrationRequest} from "../../services/models/registration-request";
import {Route, Router} from "@angular/router";
import {AuthenticationService} from "../../services/services/authentication.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {

  constructor(private router: Router,
              private authService: AuthenticationService) {
  }

  registerRequest: RegistrationRequest = {email:'', firstname:'', lastname:'', password:''};
  errorMsg: Array<string> = [];

  register() {
    this.errorMsg =[];
    this.authService.register({
      body: this.registerRequest
    }).subscribe({
      next: () => {
        this.router.navigate(['activate-account']);
      },
      error:(err) =>{
        this.errorMsg = err.error.validatonErrors;
      }
    })
  }

  login() {
    this.router.navigate(['login'])
  }
}
