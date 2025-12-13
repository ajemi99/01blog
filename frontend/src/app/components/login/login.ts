import { Component, signal } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';
import { Router,RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule,RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  usernameOrEmail = '';
  password = '';
  message = signal('');

  isError = false;

  constructor(private auth: AuthService ,private router: Router) {}
ngOnInit() {
  const token: string | null = localStorage.getItem('token');
  if (token) {
    // parse token w set currentUser
    
    this.router.navigate(['/home']);
  }
} 
  async doLogin() {
    try {

      this.isError = false;
      await this.auth.login(this.usernameOrEmail, this.password);

      this.router.navigateByUrl('/home');
    } catch (e: any) {
      this.isError = true;
      const msg = e?.error?.message || e?.message || e?.statusText || 'Erreur login';
      this.message.set(msg);
    }

  }
}
