import { Component, OnInit } from '@angular/core';

import { Solution } from '../../../models/Solution';
import { SolutionService } from '../../../services/solution.service';
import { PuzzleService } from '../../../services/puzzle.service';
import { ErrorHandlerService } from '../../../services/error-handler.service';

@Component({
  selector: 'app-my-solutions',
  templateUrl: './my-solutions.component.html',
  styleUrls: ['./my-solutions.component.css']
})
export class MySolutionsComponent implements OnInit {
  solutions: Solution[];
  isVisible = false;
  errorMessage = '';

  constructor(private solutionService: SolutionService,
              private puzzleService: PuzzleService,
              private errorHandlerService: ErrorHandlerService) {
  }

  ngOnInit() {
    this.solutionService.getMySolutions().subscribe(solutions => {
      this.solutions = solutions;
    },
    error => {
      this.errorMessage = this.errorHandlerService.handleHttpErrorResponse(error);
    });
  }

  countSpeed(seconds: number) {
    if (seconds > 59) {
      return `${Math.floor(seconds / 60)} perc ${seconds % 60} másodperc`;
    } else {
      return `${seconds} másodperc`;
    }
  }

  toggleVisible() {
    this.isVisible = !this.isVisible;
  }

}
