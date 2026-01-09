import { Component, inject } from '@angular/core';

import { Router,RouterLink} from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../auth/auth.service';
@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule,RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private authService = inject(AuthService);
  private router = inject(Router);
  errorMessage: string | null = null;


  constructor() {}
// ngOnInit() {
//   const token: string | null = localStorage.getItem('token');
//   if (token) {
//     // parse token w set currentUser
    
//     this.router.navigate(['/home']);
//   }
// } 
// هاد الفانكشن كاتنفذ ملي المستخدم كيبرك على الزر
  onLogin(form: NgForm) {
    if (form.valid) {
      this.authService.login(form.value).subscribe({
        next: (response: any) => {
          console.log('Success!', response);
          // نفترض أن الـ API كيرجع الـ token في خاصية سميتها token
          if (response.token) {
            this.errorMessage = null;
            this.authService.saveToken(response.token);
            this.router.navigate(['/home']); // صيفطو للصفحة الرئيسية بعد النجاح
          }
        },
       error: (err) => {
      // Hna fin kan-capturiw l-ghalat
      this.errorMessage = err.error.message || "Une erreur est survenue";
    }
      });
    }
  }
}
