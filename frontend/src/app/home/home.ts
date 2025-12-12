import { Component, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { CreatePostComponent } from '../components/create-post/create-post';
import { PostListComponent } from '../components/post-list/post-list';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [FormsModule, CommonModule, CreatePostComponent, PostListComponent],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {

  username = signal('');
  roles: string[] = [];
  posts: any[] = [];

  showPopup = false;
  editMode = false;
  selectedPost: any = null;

  constructor(
    private auth: AuthService,
    private router: Router,
    private http: HttpClient
  ) {
    auth.checkToken();
    const token = localStorage.getItem('token');

    if (token && token.split('.').length === 3) {
      try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        this.username.set(payload.sub);
        const roles = payload.roles || payload.role;
        this.roles = Array.isArray(roles) ? roles : [roles];
      } catch (e) {
        console.error('Erreur token', e);
        this.logout();
      }
    } else {
      this.logout();
    }
  }

  ngOnInit() {
    this.loadPosts();
  }

  loadPosts() {
    this.http.get("http://localhost:8080/api/posts").subscribe({
      next: (data: any) => {
        this.posts = data;
        console.log(data);
        
      },
      error: (err) => console.error(err)
    });
  }

  openCreatePost() {
    this.editMode = false;
    this.selectedPost = null;
    this.showPopup = true;
  }

  openEditPost(post: any) {
    this.editMode = true;
    this.selectedPost = post;
    this.showPopup = true;
  }

  onPostCreated(newPost: any) {
    if (this.editMode) {
      const index = this.posts.findIndex(p => p.id === newPost.id);
      if (index > -1) this.posts[index] = newPost;
    } else {
      this.posts.unshift(newPost);
    }

    this.closePopup();
  }

  closePopup() {
    this.showPopup = false;
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
  deletePost(id: number) {
  this.posts = this.posts.filter(post => post.id !== id);
}
}
