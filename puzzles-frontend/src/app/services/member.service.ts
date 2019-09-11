import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Member } from '../models/Member';

@Injectable({
  providedIn: 'root'
})
export class MemberService {
  baseUrl = 'http://localhost:8080/members/';


  constructor(private http: HttpClient) { }

  getTopLeaderBoard() {
    const url = this.baseUrl + 'top-leaderboard';
    return this.http.get<Member[]>(url);
  }

  getFullLeaderBoard() {
    const url = this.baseUrl + 'full-leaderboard';
    return this.http.get<Member[]>(url);
  }

  getLoggedInMember() {
    const url = this.baseUrl + 'profile';
    return this.http.get<Member>(url);
  }

  updateMember(member: Member) {
    const url = this.baseUrl + 'profile/update';
    return this.http.put<Member>(url, member);
  }
}
