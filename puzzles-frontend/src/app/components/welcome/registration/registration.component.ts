import {Component, OnInit} from '@angular/core';
import {RegistrationService} from '../../../services/registration.service';
import {NgForm} from '@angular/forms';
import {Member} from '../../../models/Member';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {
  message = '';
  submitted = false;

  constructor(private registrationService: RegistrationService) {
  }

  ngOnInit() {
  }

  onSubmit(form: NgForm) {
    const member = new Member();
    member.username = form.value['reg-username'];
    member.email = form.value['reg-email'];
    member.password = form.value['reg-password'];

    this.registrationService.registerNewMember(member).subscribe((response) => {
      if (response == null) {
        this.message = 'Ezt az email címet már regisztrálták. Ha a Tiéd, jelentkezz be!';
      } else {
        this.message = `Kedves ${response.username}! Sikeresen regisztráltál. Jelentkezz be!`;
      }
      this.submitted = true;
      form.reset();
    });
  }
}
