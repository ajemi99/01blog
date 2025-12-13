import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommentResponse } from './comment.service';

export interface Post {
  id: number;
  description: string;
  mediaUrl?: string;
  authorUsername: string;
  createdAt: string;
  showMenu?: boolean;
  updatedAt: string;
  likesCount: number;
  liked?: boolean;
  isLiking?: boolean; 
  showComments?: boolean; 
  comments?: CommentResponse[];
  newComment?: string;
}

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private apiUrl = 'http://localhost:8080/api/posts';

  constructor(private http: HttpClient) {}

  getPosts(): Observable<Post[]> {
    return this.http.get<Post[]>(this.apiUrl, { withCredentials: true });
    
  }

  createPost(description: string, file?: File): Observable<Post> {
    const formData = new FormData();
    formData.append('description', description);
    if (file) formData.append('file', file!);
    return this.http.post<Post>(this.apiUrl, formData, { withCredentials: true });
  }

  deletePost(id: number) {
  return this.http.delete(`http://localhost:8080/api/posts/${id}`);
  }

  updatePost(postId: number, description: string, file?: File) {
  const formData = new FormData();
  formData.append('description', description);

  if (file) {
    formData.append('file', file);
  }

  return this.http.put<Post>(
    `http://localhost:8080/api/posts/${postId}`,
    formData,
    { withCredentials: true }
  );
}
getMyPosts(userId: number): Observable<Post[]> {
  return this.http.get<Post[]>(`${this.apiUrl}/my-posts/${userId}`);
}


}
