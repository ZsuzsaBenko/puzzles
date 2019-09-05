import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { PuzzleComment } from '../models/PuzzleComment';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(private http: HttpClient) {
  }

  getCommentsByMember() {
    const url = 'http://localhost:8080/comments/member';
    return this.http.get<PuzzleComment[]>(url);
  }

  getCommentsByPuzzle(puzzleId: number) {
    const url = 'http://localhost:8080/comments/';
    return this.http.get<PuzzleComment[]>(url + puzzleId);
  }

  addNewComment(newComment: PuzzleComment) {
    const url = 'http://localhost:8080/comments/add';
    return this.http.post<PuzzleComment>(url, newComment);
  }
}
