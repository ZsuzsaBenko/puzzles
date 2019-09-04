import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {NgForm} from '@angular/forms';
import {Location} from '@angular/common';

import {Puzzle} from '../../models/Puzzle';
import {PuzzleService} from '../../services/puzzle.service';
import {Solution} from '../../models/Solution';
import {StarRatingComponent} from 'ng-starrating';

@Component({
  selector: 'app-puzzle-item',
  templateUrl: './puzzle-game.component.html',
  styleUrls: ['./puzzle-game.component.css']
})
export class PuzzleGameComponent implements OnInit {
  puzzle = new Puzzle();
  start: Date;
  rating = 0;
  isFetching = true;
  isSolved = false;

  constructor(private puzzleService: PuzzleService,
              private activatedRoute: ActivatedRoute,
              private location: Location) {
  }

  ngOnInit() {
    this.start = new Date();
    const id = this.activatedRoute.snapshot.params.id;
    this.puzzleService.getPuzzleById(id).subscribe(puzzle => {
      this.puzzle = puzzle;
      this.isFetching = false;
    });
  }

  cancel() {
    this.location.back();
  }

  onCheckAnswer(form: NgForm) {
    let answer = form.value.answer;
    answer = answer.trim().toLowerCase();

    form.reset();

    if (answer === this.puzzle.answer.trim().toLowerCase()) {
      this.isSolved = true;
    } else {
      console.log('Sorry, not correct');
    }
  }

  onRate($event: { oldValue: number, newValue: number, starRating: StarRatingComponent }) {
    console.log(`Old Value:${$event.oldValue}, New Value: ${$event.newValue}`);
    this.rating = $event.newValue;
  }

  onSendSolution() {
    const end = new Date();
    const diff = Math.round((end.getTime() - this.start.getTime()) / 1000);

    const solution = new Solution();
    solution.puzzle = this.puzzle;
    solution.seconds = diff;
    solution.rating = this.rating;

    console.log(solution);

  }
}
