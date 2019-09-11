import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgForm } from '@angular/forms';
import { Location } from '@angular/common';

import { Puzzle } from '../../models/Puzzle';
import { PuzzleService } from '../../services/puzzle.service';
import { Solution } from '../../models/Solution';
import { StarRatingComponent } from 'ng-starrating';
import { SolutionService } from '../../services/solution.service';
import { ErrorHandlerService } from '../../services/error-handler.service';

@Component({
  selector: 'app-puzzle-item',
  templateUrl: './puzzle-game.component.html',
  styleUrls: ['./puzzle-game.component.css']
})
export class PuzzleGameComponent implements OnInit {
  puzzle = new Puzzle();
  start: Date;
  rating = 5;
  isFetching = true;
  isSolved = false;
  isIncorrect = false;
  errorMessage = '';
  showError = false;
  failedToSendSolution = false;

  constructor(private puzzleService: PuzzleService,
              private solutionService: SolutionService,
              private errorHandlerService: ErrorHandlerService,
              private activatedRoute: ActivatedRoute,
              private location: Location) {
  }

  ngOnInit() {
    this.start = new Date();
    const id = this.activatedRoute.snapshot.params.id;
    this.puzzleService.getPuzzleById(id).subscribe(puzzle => {
      this.puzzle = puzzle;
      this.isFetching = false;
    },
    error => {
      this.errorMessage = this.errorHandlerService.handleHttpErrorResponse(error);
      this.showError = true;
      this.isFetching = false;
    });
  }

  onCancel() {
    this.location.back();
  }

  onCheckAnswer(form: NgForm) {
    let answer = form.value.answer;
    answer = answer.trim().toLowerCase();

    form.reset();

    if (answer === this.puzzle.answer.trim().toLowerCase()) {
      this.isSolved = true;
    } else {
      this.isIncorrect = true;
      setTimeout(() => this.isIncorrect = false, 3000);
    }
  }

  onRate($event: { oldValue: number, newValue: number, starRating: StarRatingComponent }) {
    this.rating = $event.newValue;
  }

  onSendSolution() {
    const end = new Date();
    const diff = Math.round((end.getTime() - this.start.getTime()) / 1000);

    const solution = new Solution();
    solution.puzzle = this.puzzle;
    solution.seconds = diff;
    solution.rating = this.rating;

    this.solutionService.sendSolution(solution).subscribe(() => {
      this.location.back();
    },
    error => {
      this.errorMessage = this.errorHandlerService.handleHttpErrorResponse(error);
      this.failedToSendSolution = true;
    });
  }
}
