import { Component, signal } from '@angular/core';
import { AuthService } from '../../auth/auth.service';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-register',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
   username = '';
  email = '';
  password = '';
  message = signal('');
  loading = false;
  isError = false;

  constructor(private auth: AuthService, private router: Router) {}

  async doRegister() {
    try {
      // this.loading = true;
      this.isError = false;
      await this.auth.register(this.username, this.email, this.password);
      this.message.set('Inscription OK');
      this.router.navigateByUrl('/login');
    } catch (e: any) {
      this.isError = true;
      const msg = e?.error?.message || e?.message || e?.statusText || 'Erreur inscription';
      
      this.message.set(msg);
    }
    // this.loading = false;
  }
}
