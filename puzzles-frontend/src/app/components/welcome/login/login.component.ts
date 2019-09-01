import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../../services/auth.service';
import {NgForm} from '@angular/forms';
import {Router} from '@angular/router';

@Component({
  selector: 'app-reg-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  message = '';
  submitted = false;

  constructor(private authService: AuthService,
              private router: Router) {
  }

  ngOnInit() {
  }

  onSubmit(form: NgForm) {
    const data = {
      email: form.value['login-email'],
      password: form.value['login-password']
    };

    this.authService.login(data).subscribe( (response: any) => {
        localStorage.setItem('token', response.token);
        form.reset();
        this.router.navigate(['/home']).then();
      },
      () => {
        this.message = 'Érvénytelen email cím vagy jelszó. Próbáld újra!';
        this.submitted = true;
        form.reset();
    });
  }

}
