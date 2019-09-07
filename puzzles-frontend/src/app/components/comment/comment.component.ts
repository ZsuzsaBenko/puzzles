import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { CommentService } from '../../services/comment.service';
import { ErrorHandlerService } from '../../services/error-handler.service';
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
  errorMessage = '';
  showError = false;
  failedToAddComment = false;

  constructor(private commentService: CommentService,
              private errorHandlerService: ErrorHandlerService,
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
    },
    error => {
      this.errorMessage = this.errorHandlerService.handleHttpErrorResponse(error);
      this.showError = true;
      this.isFetching = false;
    });
  }

  onSubmit(form: NgForm) {
    const agreement = form.value.agreement;
    if (!agreement) {
      return;
    }

    const message = form.value.message;
    const puzzle = new Puzzle();
    puzzle.id = this.puzzleId;

    const newPuzzleComment = new PuzzleComment();
    newPuzzleComment.message = message;
    newPuzzleComment.puzzle = puzzle;

    this.commentService.addNewComment(newPuzzleComment).subscribe( response => {
      form.reset();
      this.comments.push(response);
    },
      error => {
      this.errorMessage = this.errorHandlerService.handleHttpErrorResponse(error);
      this.failedToAddComment = true;
    });
  }
}
