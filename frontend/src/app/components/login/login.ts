import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router,RouterLink} from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth/auth.service';
@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule,RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login implements OnInit {
  private authService = inject(AuthService);
  private router = inject(Router);
  errorMessage: string | null = null;
  private route = inject(ActivatedRoute);


  constructor() {}
  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      const reason = params['reason'];
      if (reason === 'deleted') {
        this.errorMessage = "Hada l-account ma-bqach mawjoud (T-msa7).";
      } else if (reason === 'banned') {
        this.errorMessage = "Had l-account t-banna mn l-Admin.";
      } else if (reason === 'expired') {
        this.errorMessage = "Session dyalk salat, 3awed d-dir login.";
      }
    });
} 
// هاد الفانكشن كاتنفذ ملي المستخدم كيبرك على الزر
  onLogin(form: NgForm) {
    if (form.valid) {
      this.authService.login(form.value).subscribe({
        next: (response: any) => {
          console.log('Success!', response);
          // نفترض أن الـ API كيرجع الـ token في خاصية سميتها token
          if (response.token) {
            this.errorMessage = null;
            this.router.navigate(['/home']); // صيفطو للصفحة الرئيسية بعد النجاح
          }
        },
        error: (err) => {
          if (err.status === 401) {
            // Hadi dial password/identifier ghalat
            this.errorMessage = "Identifiants incorrects. Veuillez réessayer.";
          } else if (err.status === 403) {
            // Hadi dial l-banni (l-message ghadi i-ji mn l-Back: "Votre compte est banni")
            
            this.errorMessage = err.error?.message || "Accès refusé.";
          } else {
            this.errorMessage = "Une erreur technique est survenue.";
          }
        }
      });
    }
  }
}
