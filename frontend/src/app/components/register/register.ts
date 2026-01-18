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

  onRegister(form: NgForm) {
    this.authService.register(form.value).subscribe({
      next: (res: any) => {
        if (res.token) {
          this.errorMessage = null;
          this.router.navigate(['/home']);
        }
      },
      error: (err) => {
        // 1. Ila kān mouchkil dial Validation (400)
        if (err.status === 400) {
         
          this.errorMessage = err.error?.message || "Une erreur est survenue.";
        }
      }
    });
  }
}
