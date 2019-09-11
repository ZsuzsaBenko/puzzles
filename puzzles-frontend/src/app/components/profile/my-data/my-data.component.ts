import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { NgForm } from '@angular/forms';

import { MemberService } from '../../../services/member.service';
import { Member } from '../../../models/Member';
import { ErrorHandlerService } from '../../../services/error-handler.service';

@Component({
  selector: 'app-my-data',
  templateUrl: './my-data.component.html',
  styleUrls: ['./my-data.component.css']
})
export class MyDataComponent implements OnInit {
  member = new Member();
  isFormVisible = false;
  invalidPassword = false;
  isFetching = true;
  errorMessage = '';
  showError = false;
  failedToModifyData = false;

  constructor(private memberService: MemberService,
              private errorHandlerService: ErrorHandlerService) {
  }

  ngOnInit() {
    this.memberService.getLoggedInMember().subscribe(member => {
      this.member = member;
      this.isFetching = false;
    },
    error => {
      this.errorMessage = this.errorHandlerService.handleHttpErrorResponse(error);
      this.isFetching = false;
      this.showError = true;
    });
  }


  toggleFormVisible() {
    this.isFormVisible = !this.isFormVisible;
  }

  onChangeUserData(form: NgForm) {
    const username = form.value.username;
    const newPassword = form.value.pass;
    const newPasswordConfirmed = form.value.confirmpass;

    if (username === '' && newPassword === '') {
      return;
    } else if (newPassword !== '' && newPassword !== newPasswordConfirmed) {
      this.invalidPassword = true;
      return;
    }

    const member = new Member();
    if (username !== '') {
      member.username = username;
    }
    if (newPassword !== '' && newPassword === newPasswordConfirmed) {
      member.password = newPassword;
    }

    this.updateMember(form, member);
  }

  updateMember(form: NgForm, member: Member) {
    this.memberService.updateMember(member).subscribe(() => {
        form.reset();
        this.invalidPassword = false;
        this.isFormVisible = false;
        this.memberService.getLoggedInMember().subscribe(updatedMember => {
            this.member = updatedMember;
          },
          error =>  {
            this.onError(error, form);
          });
      },
      error => {
        this.onError(error, form);
      });
  }

  onError(error: HttpErrorResponse, form: NgForm) {
    this.errorMessage = this.errorHandlerService.handleHttpErrorResponse(error);
    form.reset();
    this.failedToModifyData = true;
  }
}
