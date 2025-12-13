import { Component, signal } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';
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
  isError = false;

  constructor(private auth: AuthService, private router: Router) {}
ngOnInit() {
  const token: string | null = localStorage.getItem('token');
  if (token) {
    
    this.router.navigate(['/home']);
  }
} 
  async doRegister() {
    try {
      this.isError = false;
      await this.auth.register(this.username, this.email, this.password);
      this.message.set('Inscription OK');
      this.router.navigateByUrl('/login');
    } catch (e: any) {
      this.isError = true;
      const msg = e?.error?.message || e?.message || e?.statusText || 'Erreur inscription';
      
      this.message.set(msg);
    }
    
  }
}
