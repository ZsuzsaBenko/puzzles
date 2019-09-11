import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  url = 'http://localhost:8080/login';

  constructor(private http: HttpClient) {
  }

  login(data: {email: string, password: string}) {
    return this.http.post(this.url, data);
  }
}
