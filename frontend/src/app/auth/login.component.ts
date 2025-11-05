import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../auth.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  template: `
    <div class="auth-wrapper">
      <div class="card">
        <h2>Connexion</h2>
        <form (ngSubmit)="doLogin()">
          <label>Username ou Email</label>
          <input class="input" placeholder="ex: demo ou demo@mail.com" [(ngModel)]="usernameOrEmail" name="usernameOrEmail" required />

          <label>Mot de passe</label>
          <input class="input" placeholder="Votre mot de passe" type="password" [(ngModel)]="password" name="password" required />

          <button type="submit" class="btn primary" >Se connecter</button>
        </form>

        <p class="muted">Pas de compte ?
          <a routerLink="/register" class="link">Créer un compte</a>
        </p>
        <p class="status" [class.error]="isError">{{ message() }}</p>
      </div>
    </div>
  `,
  styles: [`
    .auth-wrapper { min-height: 100dvh; display: grid; place-items: center; background: #fafafa; padding: 1rem; }
    .card { width: 100%; max-width: 380px; background: #fff; border: 1px solid #eee; border-radius: 12px; padding: 1.25rem 1.25rem 1rem; box-shadow: 0 6px 18px rgba(0,0,0,0.06); }
    h2 { margin: 0 0 1rem 0; font-weight: 600; font-size: 1.35rem; color: #222; }
    form { display: grid; gap: .75rem; }
    label { font-size: .85rem; color: #444; }
    .input { width: 100%; padding: .625rem .75rem; border: 1px solid #ddd; border-radius: 8px; outline: none; transition: border-color .2s, box-shadow .2s; background: #fff; }
    .input:focus { border-color: #7c3aed; box-shadow: 0 0 0 3px rgba(124,58,237,.12); }
    .btn { display: inline-flex; align-items: center; justify-content: center; gap: .5rem; padding: .625rem .9rem; border-radius: 8px; border: none; cursor: pointer; font-weight: 600; }
    .btn.primary { background: #7c3aed; color: #fff; }
    .btn.primary:disabled { opacity: .7; cursor: not-allowed; }
    .muted { margin: .75rem 0 0; color: #666; font-size: .9rem; }
    .link { color: #7c3aed; text-decoration: none; margin-left: .25rem; }
    .link:hover { text-decoration: underline; }
    .status { margin-top: .5rem; font-size: .9rem; color: #2563eb; }
    .status.error { color: #dc2626; }
  `]
})
export class LoginComponent {
  usernameOrEmail = '';
  password = '';
  message = signal('');
  // loading = false;
  isError = false;

  constructor(private auth: AuthService) {}

  async doLogin() {
    try {
      // this.loading = true;
      this.isError = false;
      await this.auth.login(this.usernameOrEmail, this.password);
      this.message.set('Connecté');
    } catch (e: any) {
      this.isError = true;
      const msg = e?.error?.message || e?.message || e?.statusText || 'Erreur login';
      this.message.set(msg);
    }
    // this.loading = false;
  }
}


