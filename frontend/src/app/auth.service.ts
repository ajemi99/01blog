import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly baseUrl = 'http://localhost:8080/api/auth';
  private readonly tokenKey = 'token';

  constructor(private http: HttpClient) {}

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
}
