import {Component, OnInit} from '@angular/core';

import {Member} from '../../../models/Member';
import {MemberService} from '../../../services/member.service';

@Component({
  selector: 'app-leaderboard',
  templateUrl: './leaderboard.component.html',
  styleUrls: ['./leaderboard.component.css']
})
export class LeaderboardComponent implements OnInit {
  members: Member[];
  loggedInMember: Member;
  showAll: boolean;

  constructor(private memberService: MemberService) {
  }

  ngOnInit() {
    this.memberService.getLeaderBoard().subscribe(members => this.members = members);
    this.memberService.getLoggedInMember().subscribe(member => this.loggedInMember = member);
    this.showAll = false;
  }

  toggleShowAll() {
    this.showAll = !this.showAll;
  }

}
