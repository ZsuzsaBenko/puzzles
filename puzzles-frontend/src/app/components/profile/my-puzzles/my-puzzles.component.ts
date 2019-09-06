import { Component, OnInit } from '@angular/core';

import { Puzzle } from '../../../models/Puzzle';
import { PuzzleService } from '../../../services/puzzle.service';
import { ErrorHandlerService } from '../../../services/error-handler.service';

@Component({
  selector: 'app-my-puzzles',
  templateUrl: './my-puzzles.component.html',
  styleUrls: ['./my-puzzles.component.css']
})
export class MyPuzzlesComponent implements OnInit {
  isVisible = false;
  puzzles: Puzzle[];
  errorMessage = '';

  constructor(private puzzleService: PuzzleService,
              private errorHandlerService: ErrorHandlerService) { }

  ngOnInit() {
    this.puzzleService.getPuzzlesByMember().subscribe( puzzles => {
      this.puzzles = puzzles;
    },
    error => {
      this.errorMessage = this.errorHandlerService.handleHttpErrorResponse(error);
    });
  }

  toggleVisible() {
    this.isVisible = !this.isVisible;
  }
}
