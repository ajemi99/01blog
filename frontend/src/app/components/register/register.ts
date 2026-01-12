import { Component, inject } from '@angular/core';

import { Router,RouterLink} from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-register',
  imports: [CommonModule, FormsModule,RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  private authService = inject(AuthService);
  private router = inject(Router);
  errorMessage: string | null = null;

  constructor() {}
onRegister(form: NgForm) {
  this.authService.register(form.value).subscribe({
    next: (res:any) => {
      // this.router.navigate(['/login'], { queryParams: { registered: 'true' } });
          if (res.token) {
            this.errorMessage = null;
            this.router.navigate(['/home']); // صيفطو للصفحة الرئيسية بعد النجاح
          }
    },
    error: (err) => {
      this.errorMessage = err.error.message; // "Email already registered"
    }
  });
}
}
