import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
@Component({
  selector: 'app-create-post',
  imports: [FormsModule, CommonModule],
  templateUrl: './create-post.html',
  styleUrl: './create-post.css',
})
export class CreatePost {
  title = '';
  content = '';

  constructor(private http: HttpClient, private router: Router) {}

  createPost() {
    const post = { title: this.title, content: this.content };
    this.http.post('http://localhost:8080/api/posts', post).subscribe({
      next: () => {
        alert('✅ Post créé avec succès !');
        window.location.href = "home";
      },
      error: (err) => {
        console.error('Erreur création post', err);
        alert('❌ Erreur lors de la création du post');
      }
    });
  }
}
