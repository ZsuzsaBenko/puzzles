import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Puzzle } from '../models/Puzzle';
import { Category } from '../models/Category';
import { Level } from '../models/Level';

@Injectable({
  providedIn: 'root'
})
export class PuzzleService {
  baseUrl = 'http://localhost:8080/puzzles/';


  constructor(private http: HttpClient) {
  }

  getRandomPuzzles() {
    const url = this.baseUrl + 'random';
    return this.http.get<Puzzle[]>(url);
  }

  getAllPuzzles() {
    const url = this.baseUrl + 'all';
    return this.http.get<Puzzle[]>(url);
  }

  getPuzzlesByCategory(category: Category) {
    return this.http.get<Puzzle[]>(this.baseUrl + category.toString());
  }

  getSortedPuzzles(category: Category, sortingParam: string) {
    const url = this.baseUrl + 'sort/';
    if (!category) {
      return this.http.get<Puzzle[]>(url + sortingParam);
    } else {
      return this.http.get<Puzzle[]>(url + category + '/' + sortingParam);
    }
  }

  getPuzzlesByMember() {
    const url = this.baseUrl + 'member';
    return this.http.get<Puzzle[]>(url);
  }

  getPuzzleById(id: number) {
    const url = this.baseUrl + 'all/';
    return this.http.get<Puzzle>(url + id);
  }

  addNewPuzzle(puzzle: Puzzle) {
    const url = this.baseUrl + 'add';
    return this.http.post<Puzzle>(url, puzzle);
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
