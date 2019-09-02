import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Puzzle} from '../models/Puzzle';

@Injectable({
  providedIn: 'root'
})
export class PuzzleService {

  constructor(private http: HttpClient) {
  }

  getRandomPuzzles() {
    const url = 'http://localhost:8080/puzzles/random';
    return this.http.get<Puzzle[]>(url);
  }
}
