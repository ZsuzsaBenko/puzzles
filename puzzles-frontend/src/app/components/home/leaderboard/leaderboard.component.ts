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

  constructor(private memberService: MemberService) {
  }

  ngOnInit() {
    this.memberService.getLeaderBoard().subscribe(members => this.members = members);

  }

}
