import { Component, OnInit } from '@angular/core';
import { PostService,Post } from '../../services/post.service';
import { AuthService } from '../../auth/auth.service'; 
import { CommentService } from '../../services/comment.service';
import { PostListComponent } from '../post-list/post-list';
import { Router } from '@angular/router';

@Component({
  selector: 'app-my-posts',
  templateUrl: './my-posts.html',
  styleUrls: ['./my-posts.css'],
   imports: [PostListComponent],
   standalone: true,
})
export class MyPostsComponent implements OnInit {
  posts: Post[] = [];
  userId: number = 0;

  constructor(
    private postService: PostService,
    private commentService: CommentService,
    public auth: AuthService,
    public router: Router
  ) {}

async ngOnInit() {
  await this.auth.checkToken(); // ensures currentUser is set
  if (!this.auth.currentUser) {
    this.router.navigate(['/login']);
    return;
  }

  this.userId = this.auth.currentUser.id;
  this.loadMyPosts();
}

  loadMyPosts() {
  
    this.postService.getMyPosts(this.userId).subscribe(posts => {
      this.posts = posts;
    });
  }
goHome() {
  this.router.navigate(['/home']);
}

}

