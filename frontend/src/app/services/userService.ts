import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';

export interface currentUser{
  id:number;
  username : string;
  email:string;
  followers:number;
  following:number;
  role:string;
}
@Injectable({
  providedIn: 'root'
})

export class UserService{
     private apiUrl = 'http://localhost:8080/api';
       private userSubject = new BehaviorSubject<any>(null);
       public currentUser$ = this.userSubject.asObservable();

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
    // 3. Had l-function t-3eyet liha ghir mra wa7da f app.component
  loadCurrentUser(): Observable<currentUser> {
    return this.http.get<currentUser>(`${this.apiUrl}/users/me`).pipe(
      tap(user => {
        this.userSubject.next(user); // Khzen l-user f l-khzana
      })
    );
  }
    get currentUserValue() {
    return this.userSubject.value;
  }
}

