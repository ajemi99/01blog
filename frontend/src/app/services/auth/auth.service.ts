import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly apiUrl = 'http://localhost:8080/api/auth';
  private readonly tokenKey = 'token';
  private http = inject(HttpClient)
  currentUser: any = null;

  constructor() {
  }

  get token(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  isAuthenticated(): boolean {
    return !!this.token;
  }

  // Function ديال الـ Login
  login(credentials: any) {
    return this.http.post(`${this.apiUrl}/login`, credentials);
  }
  //Function register
  register(credentials:any){
    return this.http.post(`${this.apiUrl}/register`, credentials)
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
  
}