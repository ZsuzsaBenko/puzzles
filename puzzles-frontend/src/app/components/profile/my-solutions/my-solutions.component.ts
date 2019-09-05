import {Component, OnInit} from '@angular/core';

import {Solution} from '../../../models/Solution';
import {SolutionService} from '../../../services/solution.service';
import {PuzzleService} from '../../../services/puzzle.service';

@Component({
  selector: 'app-my-solutions',
  templateUrl: './my-solutions.component.html',
  styleUrls: ['./my-solutions.component.css']
})
export class MySolutionsComponent implements OnInit {
  solutions: Solution[];
  isVisible = false;

  constructor(private solutionService: SolutionService,
              private puzzleService: PuzzleService) {
  }

  ngOnInit() {
    this.solutionService.getMySolutions().subscribe(solutions => this.solutions = solutions);
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
