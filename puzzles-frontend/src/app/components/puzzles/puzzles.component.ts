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

}
