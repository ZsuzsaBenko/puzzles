import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';

import { PuzzleService } from '../../services/puzzle.service';
import { Puzzle } from '../../models/Puzzle';

@Component({
  selector: 'app-add-puzzle',
  templateUrl: './add-puzzle.component.html',
  styleUrls: ['./add-puzzle.component.css']
})
export class AddPuzzleComponent implements OnInit {
  puzzle = new Puzzle();
  isNormalPuzzle = false;
  isPicturePuzzle = false;
  isCipher = false;

  constructor(private puzzleService: PuzzleService) { }

  ngOnInit() {
  }

  onChangeCategory(form: NgForm) {
    if (form.value.category === 'PICTURE_PUZZLE') {
      this.isNormalPuzzle = false;
      this.isPicturePuzzle = true;
      this.isCipher = false;
    } else if (form.value.category === 'CIPHER') {
      this.isNormalPuzzle = false;
      this.isPicturePuzzle = false;
      this.isCipher = true;
    } else {
      this.isNormalPuzzle = true;
      this.isPicturePuzzle = false;
      this.isCipher = false;
    }
  }

  onSubmit(form: NgForm) {
    this.puzzle.title = form.value.title;
    this.puzzle.category = form.value.category;
    this.puzzle.level = form.value.level;
    this.puzzle.instruction = form.value.instruction;
    this.puzzle.puzzleItem = form.value.puzzleItem;
    this.puzzle.answer = form.value.answer;

  }
}
