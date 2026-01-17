import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ReportResponse{
    id:number;
    reason: string;
    reporterUsername: string;
    reportedUsername: string;
    postId: number;
    createdAt: string;

}
export interface UserDto{
        id : number;
        username: string;
        email: string
        role: string
         banned :Boolean
}
export interface PostDto{
    id:number;
    description: string;
     author : string;
     createdAt: string
}

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private http = inject(HttpClient);
  // L-URL dial l-Backend nichan
  private apiUrl = 'http://localhost:8080/api/admin';

  // 1. Jbed ga3 l-reports li m-khbyine f l-Database
  getAllReports(): Observable<ReportResponse[]> {
    return this.http.get<ReportResponse[]>(`${this.apiUrl}/reports`);
  }

  // 2. Sift l-qarar dial l-Admin l l-Back (Action)
  takeAction(reportId: number, action: string): Observable<void> {
    // Kat-sift l-id dial report w l-action (delete_post, ban_user...)
    return this.http.post<void>(`${this.apiUrl}/reports/${reportId}/action?action=${action}`, {});
  }
    // Jbed ga3 l-users
    getAllUsers(): Observable<UserDto[]> {
    return this.http.get<UserDto[]>(`${this.apiUrl}/users`);
    }

    // Banni user
    banUser(userId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/users/${userId}/ban`, {});
    }

    // Unban user
    unbanUser(userId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/users/${userId}/unban`, {});
    }

    // Delete user
    deleteUser(userId: number): Observable<void> {
         return this.http.delete<void>(`${this.apiUrl}/users/${userId}`);
    }
     getAllPosts(): Observable<PostDto[]> {
        return this.http.get<PostDto[]>(`${this.apiUrl}/posts`);
    }
    deletePost(postId:number): Observable<void>{
        return this.http.delete<void>(`${this.apiUrl}/posts/${postId}`);
    }
}