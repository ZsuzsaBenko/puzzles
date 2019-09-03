import {Component, OnInit} from '@angular/core';
import {Puzzle} from '../../../models/Puzzle';
import {PuzzleService} from '../../../services/puzzle.service';
import {Category} from '../../../models/Category';
import {Level} from '../../../models/Level';

@Component({
  selector: 'app-random-puzzles',
  templateUrl: './random-puzzles.component.html',
  styleUrls: ['./random-puzzles.component.css']
})
export class RandomPuzzlesComponent implements OnInit {
  puzzles: Puzzle[];
  currentPuzzle = new Puzzle();
  isPrevious = false;
  isNext = true;

  constructor(private puzzleService: PuzzleService) {
  }

  ngOnInit() {
    this.puzzleService.getRandomPuzzles().subscribe(puzzles => {
      this.puzzles = puzzles;
      this.currentPuzzle = puzzles[0];
    });
  }

  stepRight() {
    if (!this.isNext) {
      return;
    }

    const index = this.puzzles.indexOf(this.currentPuzzle);
    if (index < this.puzzles.length - 1) {
      this.currentPuzzle = this.puzzles[index + 1];
      this.isPrevious = true;
    }
    if (index === this.puzzles.length - 1) {
      this.isNext = false;
    }
  }

  stepLeft() {
    if (!this.isPrevious) {
      return;
    }

    const index = this.puzzles.indexOf(this.currentPuzzle);
    if (index > 0) {
      this.currentPuzzle = this.puzzles[index - 1];
      this.isNext = true;
    }
    if (index === 0) {
      this.isPrevious = false;
    }
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
