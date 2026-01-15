import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';



@Injectable({
  providedIn: 'root'
})
export class PostService {
  private apiUrl = 'http://localhost:8080/api/posts';

  constructor(private http: HttpClient) {}

    createPost(description: string, file?: File): Observable<any> {
      const formData = new FormData();
      formData.append('description', description);
      if (file) {
        formData.append('file', file);
      }
      return this.http.post(this.apiUrl, formData);
    }
    
    getFeed(): Observable<any[]> {
      return this.http.get<any[]>(`${this.apiUrl}/feed`);
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

      return this.http.put<any>(
        `http://localhost:8080/api/posts/${postId}`,
        formData,
        { withCredentials: true }
      );
    }
}
