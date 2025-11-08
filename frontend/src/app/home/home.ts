import { Component, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-home',
  standalone: true,
  template: `
    <div class="home">
      <h2>Bienvenue, {{ username() }}</h2>
      <button (click)="logout()">Déconnexion</button>
    </div>
  `
})
export class Home {
  username = signal('');

  constructor(
    private http: HttpClient,
    private router: Router,
    private auth: AuthService  // inject AuthService
  ) {
    this.loadUser();
  }

  async loadUser() {
    try {
      const token = localStorage.getItem('token');
      if (!token) throw new Error('No token');

      const res = await this.http.get<{ username: string }>(
        'http://localhost:8080/api/auth/me',
        { headers: { Authorization: 'Bearer ' + token } }
      ).toPromise();

      if (!res?.username) throw new Error('User not found');
      this.username.set(res.username);

    } catch (e) {
      localStorage.removeItem('token');
      this.router.navigate(['/login']);
    }
  }

  logout() {
    this.auth.logout();   // remove token
    this.router.navigate(['/login']); // redirect
  }
}
