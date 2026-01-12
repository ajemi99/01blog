import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom, BehaviorSubject, Observable, tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly apiUrl = 'http://localhost:8080/api/auth';
  private readonly tokenKey = 'token';
  private http = inject(HttpClient)
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
        this.loadCurrentUser().subscribe();
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

  // async register(username: string, email: string, password: string): Promise<void> {
  //   const res = await firstValueFrom(this.http.post<{ token: string }>(`${this.apiUrl}/register`, { username, email, password }));
  //   localStorage.setItem(this.tokenKey, res.token);
  // }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
  }
  async checkToken(): Promise<void> {
    const token = this.token;
    if (!token) {
      this.logout();
      return;
    }

    try {
      // On envoie la requête vers le backend pour vérifier la validité du token
      const user = await firstValueFrom(this.http.get<{ id: number; username: string }>(`${this.apiUrl}/me`));
      this.currentUser = user;
      console.log("User", this.currentUser);
    } catch (err) {
      console.error('Token invalide ou expiré ❌', err);
      this.logout(); // redirection vers login
      window.location.href = '/login';
    }
  }
  getCurrentUser(): Observable<any> {

  return this.http.get(`${this.apiUrl}/me`);
}
}