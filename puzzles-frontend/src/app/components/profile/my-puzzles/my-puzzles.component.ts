import { Component, OnInit } from '@angular/core';
import {Puzzle} from '../../../models/Puzzle';
import {PuzzleService} from '../../../services/puzzle.service';

@Component({
  selector: 'app-my-puzzles',
  templateUrl: './my-puzzles.component.html',
  styleUrls: ['./my-puzzles.component.css']
})
export class MyPuzzlesComponent implements OnInit {
  isVisible = false;
  puzzles: Puzzle[];

  constructor(private puzzleService: PuzzleService) { }

  ngOnInit() {
    this.puzzleService.getPuzzlesByMember().subscribe( puzzles => this.puzzles = puzzles);
  }

  toggleVisible() {
    this.isVisible = !this.isVisible;
  }
}
