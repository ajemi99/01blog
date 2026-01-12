import { Component, signal, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { CreatePostComponent } from '../components/create-post/create-post';
import { PostListComponent } from '../components/post-list/post-list';
import { Sidebar } from '../components/sidebar/sidebar';
import { PostService } from '../services/post.service';
import { log } from 'console';
// import { NgOptimizedImage } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [FormsModule, CommonModule, CreatePostComponent],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {

//   username = signal('');
//   roles: string[] = [];
//   posts: any[] = [];
//   showMyPosts: boolean = false;
//   showPopup = false;
//   editMode = false;
//   selectedPost: any = null;

//   constructor(
//     private auth: AuthService,
//     private router: Router,
//     private http: HttpClient
//   ) {
//     auth.checkToken();
//     const token = localStorage.getItem('token');

//     if (token && token.split('.').length === 3) {
//       try {
//         const payload = JSON.parse(atob(token.split('.')[1]));
//         this.username.set(payload.sub);
//         const roles = payload.roles || payload.role;
//         this.roles = Array.isArray(roles) ? roles : [roles];
//       } catch (e) {
//         console.error('Erreur token', e);
//         this.logout();
//       }
//     } else {
//       this.logout();
//     }
//   }

//   goToMyPosts() {
//     console.log(this.username);
    
//     this.router.navigate(['/my-posts']);
//   }
//   ngOnInit() {
//     this.loadPosts();
//   }

// loadPosts() {
//   console.log("User roles:", this.roles);

//   if (!this.roles || this.roles.length === 0) {
//     console.warn("No roles found, default to user feed");
//   }

//   let url = "http://localhost:8080/api/posts/feed"; // default user feed
//   // if (this.roles && this.roles.includes("ADMIN")) {
//   //   url = "http://localhost:8080/api/admin/posts"; // admin sees all
//   // }

//   this.http.get(url).subscribe({
//     next: (data: any) => {
//       this.posts = data;
//       console.log(data);
//     },
//     error: (err) => console.error(err)
//   });
// }



//   openCreatePost() {
//     this.editMode = false;
//     this.selectedPost = null;
//     this.showPopup = true;
//   }

//   openEditPost(post: any) {
//     this.editMode = true;
//     this.selectedPost = post;
//     this.showPopup = true;
//   }

//   onPostCreated(newPost: any) {
//     if (this.editMode) {
//       const index = this.posts.findIndex(p => p.id === newPost.id);
//       if (index > -1) this.posts[index] = newPost;
//     } else {
//       this.posts.unshift(newPost);
//     }

//     this.closePopup();
//   }

//   closePopup() {
  //     this.showPopup = false;
  //   }
  
  //   logout() {
    //     localStorage.removeItem('token');
    //     this.router.navigate(['/login']);
    //   }
    //   deletePost(id: number) {
      //   this.posts = this.posts.filter(post => post.id !== id);
      // }
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

  onDelete(postId: number) {
    if (confirm('Are you sure you want to delete this post?')) {
      this.postService.deletePost(postId).subscribe({
        next: () => {
          this.posts = this.posts.filter(p => p.id !== postId);
        },
        error: (err) => alert('Error deleting post')
      });
    }
  }

    isImage(fileUrl: string | undefined): boolean {
    if (!fileUrl) return false;
    const ext = fileUrl.split('.').pop()?.toLowerCase();
    return ['jpg','jpeg','png','gif'].includes(ext!);
  }

  isVideo(fileUrl: string | undefined): boolean {
    if (!fileUrl) return false;
    const ext = fileUrl.split('.').pop()?.toLowerCase();
    return ['mp4','mov','webm'].includes(ext!);
  }
  // Function bach n-check-iw wach moul l-post
isOwner(post: any): boolean {
    if (!this.currentUser || !post) return false;
    // Kandiro l-check b s-miya (Username) kima 3ndek f l-objer
    return post.authorUsername === this.currentUser.username;
  }

}
