import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
};


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  url = 'http://localhost:8080/login';

  constructor(private http: HttpClient) {
  }

  login(data: {email: string, password: string}) {
    return this.http.post(this.url, data, httpOptions);
  }
}
