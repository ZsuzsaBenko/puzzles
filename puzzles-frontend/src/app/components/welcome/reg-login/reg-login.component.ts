import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';

import { Member } from '../../../models/Member';
import { AuthService } from '../../../services/auth.service';
import { RegistrationService } from '../../../services/registration.service';
import { ErrorHandlerService } from '../../../services/error-handler.service';

@Component({
  selector: 'app-reg-login',
  templateUrl: './reg-login.component.html',
  styleUrls: ['./reg-login.component.css']
})
export class RegLoginComponent implements OnInit {
  @ViewChild('form', {static: true}) form;
  isLogin = true;
  message = '';

  constructor(private registrationService: RegistrationService,
              private authService: AuthService,
              private errorHandlerService: ErrorHandlerService,
              private router: Router) { }

  ngOnInit() {
  }

  showLoginForm() {
    if (this.isLogin !== true) {
      this.isLogin = true;
      this.reset();
    }
  }

  showRegistrationForm() {
    if (this.isLogin === true) {
      this.isLogin = false;
      this.reset();
    }
  }

  reset() {
    this.message = '';
    this.form.reset();
    this.form.submitted = false;
  }

  onSubmit(form: NgForm) {
    if (this.isLogin) {
      const data = {
        email: form.value.email.toLowerCase(),
        password: form.value.password
      };

      this.login(data);

    } else {
      const member = new Member();
      member.username = form.value.username;
      member.email = form.value.email.toLowerCase();
      member.password = form.value.password;

      this.register(member);
    }
  }

  login(data: {email: string, password: string}) {
    this.authService.login(data).subscribe( (response: any) => {
        localStorage.setItem('token', response.token);
        this.router.navigate(['/home']).then();
      },
      error => {
        this.message = this.errorHandlerService.handleHttpErrorResponse(error);
        if (this.message.startsWith('Nem vagy bejelentkezve')) {
          this.message = 'Érvénytelen email cím vagy jelszó. Próbáld újra!';
        }
        this.form.reset();
      });
  }

  register(member: Member) {
    this.registrationService.registerNewMember(member).subscribe((response) => {
      if (response == null) {
        this.message = 'Ezt az email címet már regisztrálták. Ha a Tiéd, jelentkezz be!';
      } else {
        this.message = `Kedves ${response.username}! Sikeresen regisztráltál. Jelentkezz be!`;
      }
      this.form.reset();
    }, error => {
      this.message = this.errorHandlerService.handleHttpErrorResponse(error);
      this.form.reset();
    });
  }

}
