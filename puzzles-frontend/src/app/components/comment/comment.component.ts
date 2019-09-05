import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { CommentService } from '../../services/comment.service';
import { PuzzleComment } from '../../models/PuzzleComment';
import { Puzzle } from '../../models/Puzzle';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css']
})
export class CommentComponent implements OnInit {
  comments: PuzzleComment[];
  puzzleId: number;
  isFetching = true;

  constructor(private commentService: CommentService,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit() {
    const activeUrl = this.activatedRoute.snapshot.url.toString();
    const firstComma = activeUrl.indexOf(',');
    const lastComma = activeUrl.lastIndexOf(',');
    const puzzleId = activeUrl.substring(firstComma + 1, lastComma);
    this.puzzleId = +puzzleId;

    this.commentService.getCommentsByPuzzle(this.puzzleId).subscribe(comments => {
      this.comments = comments;
      this.isFetching = false;
    });
  }

  onSubmit(form: NgForm) {
    const message = form.value.message;
    const puzzle = new Puzzle();
    puzzle.id = this.puzzleId;

    const newPuzzleComment = new PuzzleComment();
    newPuzzleComment.message = message;
    newPuzzleComment.puzzle = puzzle;

    this.commentService.addNewComment(newPuzzleComment).subscribe( response => {
      form.reset();
      this.comments.push(response);
    });
  }
}
