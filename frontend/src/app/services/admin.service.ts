  import { Injectable, inject } from '@angular/core';
  import { HttpClient } from '@angular/common/http';
  import { Observable } from 'rxjs';

  export interface ReportResponse{
      id:number;
      reason: string;
      reporterUsername: string;
      reportedUsername: string;
      reportedUserRole:string;
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
  export interface PageResponse<T> {
    content: T[];
    page: {
      size: number;
      number: number;
      totalElements: number;
      totalPages: number;
    };
  }

  @Injectable({
    providedIn: 'root'
  })
  export class AdminService {
    private http = inject(HttpClient);
    // L-URL dial l-Backend nichan
    private apiUrl = 'http://localhost:8080/api/admin';

    // 1. Jbed ga3 l-reports li m-khbyine f l-Database
    getAllReports(page: number = 0, size: number = 10): Observable<PageResponse<ReportResponse>> {
      return this.http.get<PageResponse<ReportResponse>>(`${this.apiUrl}/reports?page=${page}&size=${size}`);
    }

    // 2. Sift l-qarar dial l-Admin l l-Back (Action)
    takeAction(reportId: number, action: string): Observable<void> {
      // Kat-sift l-id dial report w l-action (delete_post, ban_user...)
      return this.http.post<void>(`${this.apiUrl}/reports/${reportId}/action?action=${action}`, {});
    }
      // Jbed ga3 l-users
      getAllUsers(page: number = 0, size: number = 10): Observable<PageResponse<UserDto>> {
      return this.http.get<PageResponse<UserDto>>(`${this.apiUrl}/users?page=${page}&size=${size}`);
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
      getAllPosts(page: number = 0, size: number = 10): Observable<PageResponse<PostDto>> {
          return this.http.get<PageResponse<PostDto>>(`${this.apiUrl}/posts?page=${page}&size=${size}`);
      }
      deletePost(postId:number): Observable<void>{
          return this.http.delete<void>(`${this.apiUrl}/posts/${postId}`);
      }
  }