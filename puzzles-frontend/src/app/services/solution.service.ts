import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Solution } from '../models/Solution';

@Injectable({
  providedIn: 'root'
})
export class SolutionService {
  baseUrl = 'http://localhost:8080/solutions/';

  constructor(private http: HttpClient) { }

  sendSolution(solution: Solution) {
    const url = this.baseUrl + 'save';
    return this.http.post<Solution>(url, solution);
  }

  getMySolutions() {
    const url = this.baseUrl + 'member';
    return this.http.get<Solution[]>(url);
  }
}
