import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

import { Member } from '../../../models/Member';
import { MemberService } from '../../../services/member.service';
import { ErrorHandlerService } from '../../../services/error-handler.service';

@Component({
  selector: 'app-leaderboard',
  templateUrl: './leaderboard.component.html',
  styleUrls: ['./leaderboard.component.css']
})
export class LeaderboardComponent implements OnInit {
  members: Member[];
  loggedInMember = new Member();
  errorMessage = '';
  showError = false;

  constructor(private memberService: MemberService,
              private errorHandlerService: ErrorHandlerService) {
  }

  ngOnInit() {
    this.memberService.getTopLeaderBoard().subscribe(members => {
      this.members = members;
    },
    error => {
      this.onError(error);
    });

    this.memberService.getLoggedInMember().subscribe(member => {
      this.loggedInMember = member;
    },
      error => {
      this.onError(error);
      });
  }

  showFullLeaderboard() {
    this.memberService.getFullLeaderBoard().subscribe(members => {
      this.members = members;
    },
      error => {
        this.onError(error);
      });
  }

  onError(error: HttpErrorResponse) {
    this.errorMessage = this.errorHandlerService.handleHttpErrorResponse(error);
    this.showError = true;
  }

}
