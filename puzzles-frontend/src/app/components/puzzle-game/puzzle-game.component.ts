import {Component, OnInit} from '@angular/core';
import {Puzzle} from '../../models/Puzzle';
import {PuzzleService} from '../../services/puzzle.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-puzzle-item',
  templateUrl: './puzzle-game.component.html',
  styleUrls: ['./puzzle-game.component.css']
})
export class PuzzleGameComponent implements OnInit {
  puzzle = new Puzzle();
  start: Date;
  isFetching = true;

  constructor(private puzzleService: PuzzleService,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit() {
    this.start = new Date();
    const id = this.activatedRoute.snapshot.params.id;
    this.puzzleService.getPuzzleById(id).subscribe(puzzle => {
      this.puzzle = puzzle;
      this.isFetching = false;
    });
  }


  /*
  const end = new Date();
  const diff = Math.round((end.getTime() - this.start.getTime()) / 1000);
   */
}
