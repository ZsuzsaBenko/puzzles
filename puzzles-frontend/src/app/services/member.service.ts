import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Member } from '../models/Member';

@Injectable({
  providedIn: 'root'
})
export class MemberService {

  constructor(private http: HttpClient) { }

  getTopLeaderBoard() {
    const url = 'http://localhost:8080/members/top-leaderboard';
    return this.http.get<Member[]>(url);
  }

  getFullLeaderBoard() {
    const url = 'http://localhost:8080/members/full-leaderboard';
    return this.http.get<Member[]>(url);
  }

  getLoggedInMember() {
    const url = 'http://localhost:8080/members/profile';
    return this.http.get<Member>(url);
  }
}
