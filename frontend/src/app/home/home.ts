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
import { LikeService } from '../services/LikeService';
import { CommentService } from '../services/comment.service';
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
      private likeService = inject(LikeService)
      private commentService = inject(CommentService)
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
    onLike(post: any) {
      // 1. Ila kante aslan isLiking kheddama, 7bess
      if (post.isLiking) return;

      // 2. Creer l-field f l-blassa w diro true
      post.isLiking = true;

      this.likeService.toggleLike(post.id).subscribe({
        next: (res: any) => {
          // res jay fih { "message": "liked" }
          if (res.message === 'liked') {
            post.liked = true;
            post.likesCount++;
          } else if (res.message === 'unliked') {
            post.liked = false;
            post.likesCount--;
          }
          
          // 3. Rjje3ha false bach t-7iyyed spinner
          post.isLiking = false;
        },
        error: (err) => {
          console.error(err);
          post.isLiking = false;
        }
      });
    }
    
toggleComments(currentPost: any) {
  // 1. Ila l-post kan aslan m7loum, ghir n-seddoh (Toggle normal)
  if (currentPost.showComments) {
    currentPost.showComments = false;
    
    return;
  }

  // 2. Sedd ga3 l-comments dial ga3 l-posts khorine
  this.posts.forEach(p => {
    if (p.id !== currentPost.id) {
      p.showComments = false;
    }
  });

  // 3. 7ell l-comments dial l-post li cliquina 3lih
  currentPost.showComments = true;

  // 4. Fetch data ila kant khawya (nafs l-logic dial qbila)
  if (!currentPost.comments) {
    currentPost.isLoadingComments = true;
    this.commentService.getCommentsByPost(currentPost.id).subscribe({
      next: (res) => {
        currentPost.comments = res;
        currentPost.isLoadingComments = false;
      },
      error: () => currentPost.isLoadingComments = false
    });
  }
}

  onAddComment(post: any) {
    if (!post.newCommentText || !post.newCommentText.trim()) return;

    const request = {
      postId: post.id,
      content: post.newCommentText
    };

    this.commentService.addComment(request).subscribe({
      next: (newComment) => {
        if (!post.comments) post.comments = [];
        // Zid l-comment jdid l-fouq
        post.comments.unshift(newComment);
        post.newCommentText = ''; // Khwi l-input
        // Ila 3ndek commentCount f l-post, tqder t-zidou
        if(post.commentCount !== undefined) post.commentCount++;
      }
    });
  }

  onDeleteComment(post: any, commentId: number) {
    if (confirm('Voulez-vous supprimer ce commentaire ?')) {
      this.commentService.deleteComment(commentId).subscribe({
        next: () => {
          post.comments = post.comments.filter((c: any) => c.id !== commentId);
          if(post.commentCount > 0) post.commentCount--;
        }
      });
    }
  }

}
