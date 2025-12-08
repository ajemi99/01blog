import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Post {
  id: number;
  description: string;
  mediaUrl?: string;
  authorUsername: string;
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
}
