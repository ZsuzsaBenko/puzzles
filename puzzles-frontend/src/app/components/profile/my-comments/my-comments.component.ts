import { Component, OnInit } from '@angular/core';

import { PuzzleComment } from '../../../models/PuzzleComment';
import { CommentService } from '../../../services/comment.service';
import { ErrorHandlerService } from '../../../services/error-handler.service';

@Component({
  selector: 'app-my-comments',
  templateUrl: './my-comments.component.html',
  styleUrls: ['./my-comments.component.css']
})
export class MyCommentsComponent implements OnInit {
  isVisible = false;
  comments: PuzzleComment[];
  errorMessage = '';

  constructor(private commentService: CommentService,
              private errorHandlerService: ErrorHandlerService) {
  }

  ngOnInit() {
    this.commentService.getCommentsByMember().subscribe(comments => {
      this.comments = comments;
    },
      error => {
        this.errorMessage = this.errorHandlerService.handleHttpErrorResponse(error);
      });
  }

  toggleVisible() {
    this.isVisible = !this.isVisible;
  }
}
