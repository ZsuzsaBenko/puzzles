import { Component, OnInit } from '@angular/core';
import { PuzzleService } from '../../services/puzzle.service';
import {ActivatedRoute} from '@angular/router';
import {Puzzle} from '../../models/Puzzle';
import {Category} from '../../models/Category';
import {Level} from '../../models/Level';

@Component({
  selector: 'app-puzzles',
  templateUrl: './puzzles.component.html',
  styleUrls: ['./puzzles.component.css']
})
export class PuzzlesComponent implements OnInit {
  puzzles: Puzzle[];
  title = '';

  constructor(private puzzleService: PuzzleService,
              private route: ActivatedRoute) { }

  ngOnInit() {
    if (this.route.snapshot.url.toString().endsWith('all')) {
      this.puzzleService.getAllPuzzles().subscribe( puzzles => {
        this.puzzles = puzzles;
        this.title = 'Összes fejtörő';
        console.log(puzzles);
      });
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
