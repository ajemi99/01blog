import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { CreatePostComponent } from '../components/create-post/create-post';


@Component({
  selector: 'app-home',
  standalone: true,
    imports: [FormsModule, CommonModule,CreatePostComponent],
 templateUrl: './home.html',

 styleUrl: './home.css',
})
export class Home {
  title = '';
  content = '';
  username = signal('');
  roles = signal<string[]>([]);
  showCreatePost = signal(false);
showPopup: boolean = false;

  openCreatePost() {
    this.showPopup = true;
  }

  closePopup() {
    this.showPopup = false;
  }
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
  // goToCreatePost() {
  //   this.router.navigate(['/create-post']);
  // } 
  selectedFile: File | null = null;

onFileSelected(event: any) {
  const file: File = event.target.files[0]; // ناخدو أول ملف
  if (file) {
    this.selectedFile = file;
  }
}
// openCreatePost() {
//   // hadi ghadi t7al popup / wla dir chi action
//   console.log("Open create post!");
// }
 createPost() {
  if (!this.content) return;

  const body = {

    content:this.content
  }


  this.http.post('http://localhost:8080/api/posts', body).subscribe({
    next: (res) => {
      alert('✅ Post créé avec succès !');
      this.title = '';
      this.content = '';
      this.showCreatePost.set(false);
    },
    error: (err) => {
      console.error('Erreur création post', err);
      alert('❌ Erreur lors de la création du post');
    }
  });
}

}
