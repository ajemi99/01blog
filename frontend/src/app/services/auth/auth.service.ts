import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly baseUrl = 'http://localhost:8080/api/auth';
  private readonly tokenKey = 'token';
  currentUser: any = null;


  constructor(private http: HttpClient) {
    this.checkToken(); 
  }

  get token(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  isAuthenticated(): boolean {
    return !!this.token;
  }

  async login(usernameOrEmail: string, password: string): Promise<void> {
    const res = await firstValueFrom(this.http.post<{ token: string }>(`${this.baseUrl}/login`, { usernameOrEmail, password }));
    localStorage.setItem(this.tokenKey, res.token);
  }

  async register(username: string, email: string, password: string): Promise<void> {
    const res = await firstValueFrom(this.http.post<{ token: string }>(`${this.baseUrl}/register`, { username, email, password }));
    localStorage.setItem(this.tokenKey, res.token);
  }

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
      const user = await firstValueFrom(this.http.get<{ id: number; username: string }>(`${this.baseUrl}/me`));
      this.currentUser = user;
      console.log("User", this.currentUser);
    } catch (err) {
      console.error('Token invalide ou expiré ❌', err);
      this.logout(); // redirection vers login
      window.location.href = '/login';
    }
  }
  
}