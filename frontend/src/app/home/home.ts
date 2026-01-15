import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth/auth.service';
import { FormsModule } from '@angular/forms';
import { CreatePostComponent } from '../components/create-post/create-post';
import { PostService } from '../services/post.service';
import { PostCard } from '../components/post-card/post-card';


@Component({
  selector: 'app-home',
  standalone: true,
  imports: [FormsModule, CommonModule, CreatePostComponent,PostCard],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {

      posts: any[] = [];
      private postService = inject(PostService)
      private  authService = inject(AuthService)
      currentUser: any;
      constructor() {}
      
      ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
      console.log(this.currentUser);
      
    });
    this.loadFeed();
  }

    loadFeed() {
      this.postService.getFeed().subscribe({
        next: (data) => {
          this.posts = data;
          console.log('Feed loaded ✅', this.posts);
        },
        error: (err) => console.error('Error loading feed ❌', err)
      });
    }

    onNewPostCreated(post: any) {
      // Kan-qellbou wach had l-post li ja 3ndou ID déjà f l-list
      const index = this.posts.findIndex(p => p.id === post.id);

      if (index !== -1) {
        // Iyeh, hada Edit: Beddel l-post f blastou nichan
        this.posts[index] = post;
      } else {
        // La, hada Create: Zidou l-fouq
        this.posts.unshift(post);
      }
    }

    onCommentsOpened(openedPostId: number) {
      // Dour 3la ga3 l-posts li 3ndek f l-list
      this.posts.forEach(p => {
        if (p.id !== openedPostId) {
          p.showComments = false; // Sedd ay wa7ed machi houwa hada
        }
      });
    }
    onDeleteSuccess(postId: number) {
      // Kan-7iydu l-post men l-array bach i-ghber men l-Front
      this.posts = this.posts.filter(p => p.id !== postId);
    }

}
