
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
import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { PostService } from '../../services/post.service';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.html',
  styleUrls: ['./create-post.css'],
  standalone: true,
  imports: [FormsModule, HttpClientModule]
})
export class CreatePostComponent {
  @Output() postCreated = new EventEmitter<void>();
  description: string = '';
  file!: File | null;

  constructor(private postService: PostService) {}

  onFileSelected(event: any) {
    this.file = event.target.files[0];
  }

  submitPost() {
    // const formData = new FormData();
    // formData.append('description', this.description);
    // if (this.file) formData.append('file', this.file);
    
  if (!this.description.trim() && !this.file) {
    alert("مايمكنش تسيفط بوست خاوي، خاص description ولا صورة !");
    return;
  }

    this.postService.createPost(this.description,this.file?? undefined).subscribe({
      next: (res) => {
        
        // alert('Post created successfully!');
        this.description = '';
        this.file = null;
        this.postCreated.emit();
      },
      error: (err) => {
        console.error(err);
        // alert('Error creating post');
      }
    });
  }
}
