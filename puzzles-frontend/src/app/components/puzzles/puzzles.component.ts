import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {PuzzleService} from '../../services/puzzle.service';
import {Puzzle} from '../../models/Puzzle';
import {Category} from '../../models/Category';
import {NgForm} from '@angular/forms';

@Component({
  selector: 'app-puzzles',
  templateUrl: './puzzles.component.html',
  styleUrls: ['./puzzles.component.css']
})
export class PuzzlesComponent implements OnInit {
  puzzles: Puzzle[];
  title = '';
  category = null;

  constructor(private puzzleService: PuzzleService,
              private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    const url = this.activatedRoute.snapshot.url.toString();
    if (url.endsWith('all')) {
      this.puzzleService.getAllPuzzles().subscribe( puzzles => {
        this.puzzles = puzzles;
        this.title = 'Összes rejtvény';
      });
    } else if (url.endsWith('riddles')) {
      this.puzzleService.getPuzzlesByCategory(Category.RIDDLE).subscribe( puzzles => {
        this.puzzles = puzzles;
        this.category = Category.RIDDLE;
        this.title = 'Fejtörők, találós kérdések';
      });
    } else if (url.endsWith('math-puzzles')) {
      this.puzzleService.getPuzzlesByCategory(Category.MATH_PUZZLE).subscribe( puzzles => {
        this.puzzles = puzzles;
        this.category = Category.MATH_PUZZLE;
        this.title = 'Matematikai feladványok';
      });
    } else if (url.endsWith('picture-puzzles')) {
      this.puzzleService.getPuzzlesByCategory(Category.PICTURE_PUZZLE).subscribe( puzzles => {
        this.puzzles = puzzles;
        this.category = Category.PICTURE_PUZZLE;
        this.title = 'Képrejtvények';
      });
    } else if (url.endsWith('word-puzzles')) {
      this.puzzleService.getPuzzlesByCategory(Category.WORD_PUZZLE).subscribe( puzzles => {
        this.puzzles = puzzles;
        this.category = Category.WORD_PUZZLE;
        this.title = 'Nyelvi játékok';
      });
    } else if (url.endsWith('ciphers')) {
      this.puzzleService.getPuzzlesByCategory(Category.CIPHER).subscribe( puzzles => {
        this.puzzles = puzzles;
        this.category = Category.CIPHER;
        this.title = 'Titkosírás';
      });
    }
  }

  onSubmit(form: NgForm) {
    const sortingParam = form.value.sort;
    if (sortingParam !== '') {
      this.puzzleService.getSortedPuzzles(this.category, sortingParam).subscribe( puzzles => this.puzzles = puzzles);
    }
  }
}
