import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Solution } from '../models/Solution';

@Injectable({
  providedIn: 'root'
})
export class SolutionService {

  constructor(private http: HttpClient) { }

  sendSolution(solution: Solution) {
    const url = 'http://localhost:8080/solutions/save';
    return this.http.post<Solution>(url, solution);
  }

  getMySolutions() {
    const url = 'http://localhost:8080/solutions/member';
    return this.http.get<Solution[]>(url);
  }
}
