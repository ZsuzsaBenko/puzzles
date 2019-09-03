import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Puzzle} from '../models/Puzzle';
import {Category} from '../models/Category';
import {Level} from '../models/Level';

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

  getAllPuzzles() {
    const url = 'http://localhost:8080/puzzles/all';
    return this.http.get<Puzzle[]>(url);
  }

  getPuzzlesByCategory(category: Category) {
    const url = 'http://localhost:8080/puzzles/';
    return this.http.get<Puzzle[]>(url + category.toString());
  }

  translateCategory(category: Category) {
    switch (category) {
      case Category.RIDDLE.toString():
        return 'fejtörő';
      case Category.MATH_PUZZLE.toString():
        return 'matematikai feladvány';
      case Category.WORD_PUZZLE.toString():
        return 'nyelvi játék';
      case Category.PICTURE_PUZZLE.toString():
        return 'képrejtvény';
      case Category.CIPHER.toString():
        return 'titkos írás';
    }
  }

  translateLevel(level: Level) {
    switch (level) {
      case Level.EASY.toString():
        return 'könnyű';
      case Level.MEDIUM.toString():
        return 'közepes';
      case Level.DIFFICULT.toString():
        return 'nehéz';
    }
  }
}
