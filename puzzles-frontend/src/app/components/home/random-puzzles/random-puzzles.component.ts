import { Component, OnInit } from '@angular/core';

import { Puzzle } from '../../../models/Puzzle';
import { PuzzleService } from '../../../services/puzzle.service';
import { ErrorHandlerService } from '../../../services/error-handler.service';
import {HttpErrorResponse} from '@angular/common/http';

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
  errorMessage = '';
  showError = false;

  constructor(private puzzleService: PuzzleService,
              private errorHandlerService: ErrorHandlerService) {
  }

  ngOnInit() {
    this.puzzleService.getRandomPuzzles().subscribe(puzzles => {
        this.puzzles = puzzles;
        this.currentPuzzle = puzzles[0];
      },
      error => {
        this.onError(error);
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

  onError(error: HttpErrorResponse) {
    this.errorMessage = this.errorHandlerService.handleHttpErrorResponse(error);
    this.showError = true;
  }

}
