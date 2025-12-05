
// import { Component } from '@angular/core';
// import { CommonModule } from '@angular/common';
// import { FormsModule } from '@angular/forms';
// import { HttpClient } from '@angular/common/http';
// import { Router } from '@angular/router';
// @Component({
//   selector: 'app-create-post',
//   imports: [FormsModule, CommonModule],
//   templateUrl: './create-post.html',
//   styleUrl: './create-post.css',
// })
// export class CreatePost {
//   title = '';
//   content = '';

//   constructor(private http: HttpClient, private router: Router) {}

//   createPost() {
//     const post = { title: this.title, content: this.content };
//     this.http.post('http://localhost:8080/api/posts', post).subscribe({
//       next: () => {
//         alert('✅ Post créé avec succès !');
//         window.location.href = "home";
//       },
//       error: (err) => {
//         console.error('Erreur création post', err);
//         alert('❌ Erreur lors de la création du post');
//       }
//     });
//   }
// }
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.html',
  styleUrls: ['./create-post.css'],
  standalone: true,
  imports: [FormsModule, HttpClientModule]
})
export class CreatePostComponent {
  description: string = '';
  file!: File | null;

  constructor(private http: HttpClient) {}

  onFileSelected(event: any) {
    this.file = event.target.files[0];
  }

  submitPost() {
    const formData = new FormData();
    formData.append('description', this.description);
    if (this.file) formData.append('file', this.file);

    this.http.post('http://localhost:8080/api/posts', formData, { withCredentials: true }).subscribe({
      next: res => {
        console.log('Post created', res);
        alert('Post created successfully!');
      },
      error: err => {
        console.error(err);
        alert('Error creating post');
      }
    });
  }
}
