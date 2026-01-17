import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})

export class UserService{
     private apiUrl = 'http://localhost:8080/api';
    constructor(private http: HttpClient) {}
    searchUsers(query: string): Observable<any> {
        const params = new HttpParams().set('query', query);
         return this.http.get<any>(`${this.apiUrl}/users/search`, { params });
    }
    getUserProfile(username: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/users/profile/${username}`);
  }
  reportPost(postId: number, reason: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/reports/${postId}`, { reason });
  }
}

