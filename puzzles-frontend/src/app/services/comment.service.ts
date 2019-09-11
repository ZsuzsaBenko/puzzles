import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { PuzzleComment } from '../models/PuzzleComment';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  baseUrl = 'http://localhost:8080/comments/';

  constructor(private http: HttpClient) {
  }

  getCommentsByMember() {
    const url = this.baseUrl + 'member';
    return this.http.get<PuzzleComment[]>(url);
  }

  getCommentsByPuzzle(puzzleId: number) {
    return this.http.get<PuzzleComment[]>(this.baseUrl + puzzleId);
  }

  addNewComment(newComment: PuzzleComment) {
    const url = this.baseUrl + 'add';
    return this.http.post<PuzzleComment>(url, newComment);
  }
}
