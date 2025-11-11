import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-home',
  standalone: true,
    imports: [FormsModule, CommonModule],
 templateUrl: './home.html',

 styleUrl: './home.css',
})
export class Home {
  title = '';
  content = '';
  username = signal('');
  roles = signal<string[]>([]);
  showCreatePost = signal(false);

  constructor(private auth: AuthService, private router: Router,private http: HttpClient) {
    auth.checkToken();

    const token = localStorage.getItem('token');
    if (token && token.split('.').length === 3) {
      try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        this.username.set(payload.sub);
        const roles = payload.roles || payload.role;
        this.roles.set(Array.isArray(roles) ? roles : [roles]);
      } catch (e) {
        console.error('Erreur de décodage du token', e);
        this.logout();
      }
    } else {
      this.logout();
    }
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
  goToCreatePost() {
    this.router.navigate(['/create-post']);
  } 
  selectedFile: File | null = null;

onFileSelected(event: any) {
  const file: File = event.target.files[0]; // ناخدو أول ملف
  if (file) {
    this.selectedFile = file;
  }
}
 createPost() {
  if (!this.title || !this.content) return;

  const formData = new FormData();
  formData.append('title', this.title);
  formData.append('content', this.content);
  if (this.selectedFile) {
    formData.append('file', this.selectedFile);
  }

  this.http.post('http://localhost:8080/api/posts', formData).subscribe({
    next: () => {
      alert('✅ Post créé avec succès !');
      this.title = '';
      this.content = '';
      this.selectedFile = null;
      this.showCreatePost.set(false);
    },
    error: (err) => {
      console.error('Erreur création post', err);
      alert('❌ Erreur lors de la création du post');
    }
  });
}

}
