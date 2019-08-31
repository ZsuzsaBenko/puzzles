import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent implements OnInit {
  login = true;

  constructor() { }

  ngOnInit() {
  }

  loadLoginComponent() {
    if (this.login !== true) {
      this.login = true;
    }
  }

  loadRegistrationComponent() {
    if (this.login === true) {
      this.login = false;
    }
  }
}
