import { Component, OnInit } from '@angular/core';
import { CommentService } from '../../services/comment.service';
import { ActivatedRoute } from '@angular/router';
import { PuzzleComment } from '../../models/PuzzleComment';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css']
})
export class CommentComponent implements OnInit {
  comments: PuzzleComment[];
  isFetching = true;

  constructor(private commentService: CommentService,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit() {
    const activeUrl = this.activatedRoute.snapshot.url.toString();
    const firstComma = activeUrl.indexOf(',');
    const lastComma = activeUrl.lastIndexOf(',');
    const puzzleId = activeUrl.substring(firstComma + 1, lastComma);

    this.commentService.getCommentsByPuzzle(+puzzleId).subscribe(comments => {
      this.comments = comments;
      this.isFetching = false;
    });
  }

}
