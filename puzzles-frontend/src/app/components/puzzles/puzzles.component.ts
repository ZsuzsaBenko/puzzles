import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

import { PuzzleService } from '../../services/puzzle.service';
import { Puzzle } from '../../models/Puzzle';
import { Category } from '../../models/Category';
import { NgForm } from '@angular/forms';
import { ErrorHandlerService } from '../../services/error-handler.service';
import { SolutionService } from '../../services/solution.service';

@Component({
  selector: 'app-puzzles',
  templateUrl: './puzzles.component.html',
  styleUrls: ['./puzzles.component.css']
})
export class PuzzlesComponent implements OnInit {
  puzzles: Puzzle[];
  title = '';
  category = null;
  isFetching = true;
  errorMessage = '';
  showError = false;

  constructor(private puzzleService: PuzzleService,
              private solutionService: SolutionService,
              private errorHandlerService: ErrorHandlerService,
              private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    const url = this.activatedRoute.snapshot.url.toString();
    if (url.endsWith('all')) {
      this.puzzleService.getAllPuzzles().subscribe( puzzles => {
        this.puzzles = puzzles;
        this.title = 'Összes rejtvény';
        this.isFetching = false;
        this.markSolvedPuzzles();
      },
        error => {
        this.onError(error);
        });
    } else if (url.endsWith('riddles')) {
      this.puzzleService.getPuzzlesByCategory(Category.RIDDLE).subscribe( puzzles => {
        this.onSuccess(puzzles, Category.RIDDLE, 'Fejtörők, találós kérdések');
        },
        error => {
          this.onError(error);
        });
    } else if (url.endsWith('math-puzzles')) {
      this.puzzleService.getPuzzlesByCategory(Category.MATH_PUZZLE).subscribe( puzzles => {
        this.onSuccess(puzzles, Category.MATH_PUZZLE, 'Matematikai feladványok');
        },
        error => {
          this.onError(error);
        });
    } else if (url.endsWith('picture-puzzles')) {
      this.puzzleService.getPuzzlesByCategory(Category.PICTURE_PUZZLE).subscribe( puzzles => {
        this.onSuccess(puzzles, Category.PICTURE_PUZZLE, 'Képrejtvények');
        },
        error => {
          this.onError(error);
        });
    } else if (url.endsWith('word-puzzles')) {
      this.puzzleService.getPuzzlesByCategory(Category.WORD_PUZZLE).subscribe( puzzles => {
        this.onSuccess(puzzles, Category.WORD_PUZZLE, 'Nyelvi játékok');
        },
        error => {
          this.onError(error);
        });
    } else if (url.endsWith('ciphers')) {
      this.puzzleService.getPuzzlesByCategory(Category.CIPHER).subscribe( puzzles => {
        this.onSuccess(puzzles, Category.CIPHER, 'Titkosírás');
        },
        error => {
          this.onError(error);
        });
    }
  }

  markSolvedPuzzles() {
    this.solutionService.getMySolutions().subscribe(solutions => {
      for (const puzzle of this.puzzles) {
        for (const solution of solutions) {
          if (solution.puzzle.id === puzzle.id) {
            puzzle.solved = true;
            break;
          }
        }
      }
    },
      error => {
      this.onError(error);
      });
  }

  onSubmit(form: NgForm) {
    const sortingParam = form.value.sort;
    if (sortingParam !== '') {
      this.puzzleService.getSortedPuzzles(this.category, sortingParam).subscribe( puzzles => {
        this.puzzles = puzzles;
        this.markSolvedPuzzles();
      },
        error => {
          this.onError(error);
        });
    }
  }

  onSuccess(puzzles: Puzzle[], category: Category, title: string) {
    this.puzzles = puzzles;
    this.category = category;
    this.title = title;
    this.isFetching = false;
    this.markSolvedPuzzles();
  }

  onError(error: HttpErrorResponse) {
    this.errorMessage = this.errorHandlerService.handleHttpErrorResponse(error);
    this.showError = true;
    this.isFetching = false;
  }
}
