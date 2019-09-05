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

  getSortedPuzzles(category: Category, sortingParam: string) {
    const url = 'http://localhost:8080/puzzles/sort/';
    if (!category) {
      return this.http.get<Puzzle[]>(url + sortingParam);
    } else {
      return this.http.get<Puzzle[]>(url + category + '/' + sortingParam);
    }
  }

  getPuzzlesByMember() {
    const url = 'http://localhost:8080/puzzles/member';
    return this.http.get<Puzzle[]>(url);
  }

  getPuzzleById(id: number) {
    const url = 'http://localhost:8080/puzzles/all/';
    return this.http.get<Puzzle>(url + id);
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
        return 'titkosírás';
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
