import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PostResponseDTO {
    id: number;
    description:string;
    mediaUrl:string
    createdAt:string
    authorId: number
    authorUsername:string
    updatedAt:string
    liked:boolean;
    likesCount:number;
    commentCount:number;
    showComments?: boolean;
}
export interface PostPageResponse{
  content:PostResponseDTO[];
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
export class PostService {
  private apiUrl = 'http://localhost:8080/api/posts';

  constructor(private http: HttpClient) {}

    createPost(description: string, file?: File): Observable<PostResponseDTO> {
      const formData = new FormData();
      formData.append('description', description);
      if (file) {
        formData.append('file', file);
      }
      return this.http.post<PostResponseDTO>(this.apiUrl, formData);
    }
    
    getFeed(page: number = 0, size: number = 10): Observable<PostPageResponse> {
      return this.http.get<PostPageResponse>(`${this.apiUrl}/feed?page=${page}&size=${size}`);
    }
  

    deletePost(id: number): Observable<void> {
      return this.http.delete<void>(`http://localhost:8080/api/posts/${id}`);
    }

    updatePost(postId: number, description: string, file?: File): Observable<PostResponseDTO> {
      const formData = new FormData();
      formData.append('description', description);

      if (file) {
        formData.append('file', file);
      }

      return this.http.put<PostResponseDTO>(
        `http://localhost:8080/api/posts/${postId}`,
        formData,
        { withCredentials: true }
      );
    }
}
