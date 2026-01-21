import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LikeService {

  private apiUrl = 'http://localhost:8080/api/likes';

  constructor(private http: HttpClient) {}

  // 🔄 Toggle Like (like/unlike)
  toggleLike(postId: number) {
    return this.http.post<{ message: string }>(
      `${this.apiUrl}/${postId}`,
      {} // body empty
    );
  }

  // 🔢 Get likes count
  // getLikesCount(postId: number) {
  //   return this.http.get<{ likesCount: number }>(`${this.apiUrl}/${postId}`);
  // }
}
