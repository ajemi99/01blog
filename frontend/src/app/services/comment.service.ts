import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CommentRequest {
  postId: number;
  content: string;
}

export interface CommentResponse {
  id: number;
  content: string;
  username: string;
  createdAt: string;
}
export interface commentPageResponse{
  content:CommentResponse[];
   page: {
        size: number;
        number: number;      // Page l-7aliya
        totalElements: number;
        totalPages: number;   // Total dial l-pages
      };
}

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private apiUrl = 'http://localhost:8080/api/comments';

  constructor(private http: HttpClient) {}

  addComment(request: CommentRequest): Observable<CommentResponse> {
    return this.http.post<CommentResponse>(this.apiUrl, request);
  }

  getCommentsByPost(postId: number, page: number = 0, size: number = 10): Observable<commentPageResponse> {
    return this.http.get<commentPageResponse>(`${this.apiUrl}/post/${postId}?page=${page}&size=${size}`);
  }
   deleteComment(commentId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${commentId}`);
  }
}
