import { Component, OnInit } from '@angular/core';
import { PuzzleComment } from '../../../models/PuzzleComment';
import { CommentService } from '../../../services/comment.service';

@Component({
  selector: 'app-my-comments',
  templateUrl: './my-comments.component.html',
  styleUrls: ['./my-comments.component.css']
})
export class MyCommentsComponent implements OnInit {
  isVisible = false;
  comments: PuzzleComment[];

  constructor(private commentService: CommentService) {
  }

  ngOnInit() {
    this.commentService.getCommentsByMember().subscribe(comments => this.comments = comments);
  }

  toggleVisible() {
    this.isVisible = !this.isVisible;
  }
}
