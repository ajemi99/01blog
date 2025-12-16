import { Component, Input, Output, EventEmitter,OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../auth/auth.service';
import { PostService, Post } from '../../services/post.service';
 import { LikeService } from '../../services/LikeService'; 
import { CommentComponent } from '../comment/comment';
import { Router } from '@angular/router';


@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.html',
  styleUrls: ['./post-list.css'],
  standalone: true,
  imports: [CommonModule,CommentComponent]
})
export class PostListComponent{

  @Input() posts: Post[] = [];
  @Output() editRequested = new EventEmitter<Post>();
  @Output() deleteRequested = new EventEmitter<number>();
  selectedPostId: number | null = null;

openComments(post: any) {
  this.selectedPostId = post.id;
}

closeComments() {
  this.selectedPostId = null;
}
 constructor(
    private postService: PostService,
    public auth: AuthService,
   private likeService: LikeService,
   private router: Router
  ) {}


toggleLike(post: Post) {
  // إذا راه كاين request جارية → مانديروش click
  if (post.isLiking) return;  

  post.isLiking = true; // عطينا signal أن request جارية

  this.likeService.toggleLike(post.id).subscribe(
    res => {
      if (res.message === 'liked') {
        post.liked = true;
        post.likesCount++;
      } else {
        post.liked = false;
        post.likesCount--;
      }
      post.isLiking = false; // رجع button active
    },
    err => {
      console.error(err);
      post.isLiking = false; // حتى إلا كان error رجع button active
    }
  );
}


// getLikesCount(post: Post) {
//   this.likeService.getLikesCount(post.id).subscribe(res => {
//     post.likesCount = res.likesCount;
//   });
// }

  

  toggleMenu(post: any) {
    post.showMenu = !post.showMenu;
  }

  editPost(post: Post) {
    this.editRequested.emit(post);
  }

  deletePost(id: number) {
    if (!confirm("Vous voulez vraiment supprimer ce post ?")) return;

    this.postService.deletePost(id).subscribe({
      next: () => {
       this.deleteRequested.emit(id); 
      },
      error: (err) => console.error(err)
    });
  }
goToUserPosts(userId: number) {
  this.router.navigate(['/user-posts', userId]);
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
}
