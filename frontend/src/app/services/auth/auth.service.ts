import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom, BehaviorSubject, Observable, tap } from 'rxjs';
import { FollowService } from '../followService';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly apiUrl = 'http://localhost:8080/api/auth';
  private readonly tokenKey = 'token';
  private http = inject(HttpClient);
  private followService = inject(FollowService);
  currentUser: any = null;
  private userSubject = new BehaviorSubject<any>(null);
  public currentUser$ = this.userSubject.asObservable();
  constructor() {
  }
  // 3. Had l-function t-3eyet liha ghir mra wa7da f app.component
  loadCurrentUser(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/me`).pipe(
      tap(user => {
        this.userSubject.next(user); // Khzen l-user f l-khzana
      })
    );
  }
  // Helper bach tjib l-user bla subscribe (ila bghiti ghir l-valeur d l-7da)
  get currentUserValue() {
    return this.userSubject.value;
  }
  get token(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  isAuthenticated(): boolean {
    return !!this.token;
  }

login(credentials: any): Observable<any> {
  return this.http.post<any>(`${this.apiUrl}/login`, credentials).pipe(
    tap(response => {
      if (response.token) {
        // 1. Darouri t-khzen l-token houwa l-lowel bach l-Interceptor y-lqah
       this.saveToken(response.token) ;
       // 2. 3ad t-3lem l-app t-loadi l-user jdid
      this.loadCurrentUser().subscribe({
          next: (user) => {
            console.log("User loaded successfully:", user);
            // 3. 3ad 3lam l-app b l-update
            // Hna l-Sidebar ghada tlqa l-user jdid dakhél 100%
            this.followService.notifyFollowUpdate(); 
          },
          error: (err) => console.error("Error loading user after login", err)
        });
      }
    })
  );
}
  //Function register
  register(credentials:any):Observable<any>{
    return this.http.post<any>(`${this.apiUrl}/register`, credentials).pipe(
      tap(response =>{
        this.saveToken(response.token);
        this.loadCurrentUser().subscribe();
      })
    )
  }

  // Function باش نخبيو الـ JWT في المتصفح
  saveToken(token: string) {
    localStorage.setItem('token', token);
  }

  // Function باش نعرفو واش الـ User متصل (غنحتاجوها من بعد)
  getToken() {
    return localStorage.getItem('token');
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
  }
}