import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostService, Post } from '../../services/post.service';

@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.html',
  styleUrls: ['./post-list.css'],
  standalone: true,
  imports: [CommonModule]
})
export class PostListComponent {
  posts: Post[] = [];

  constructor(private postService: PostService) {
    this.loadPosts();
  }

  loadPosts() {
    this.postService.getPosts().subscribe({
      next: (data) => {this.posts = data
        console.log(data);
        
      },
      error: (err) => console.error(err)
    });
  }
  isImage(fileUrl: string | undefined): boolean {
  if (!fileUrl) return false;
  const ext = fileUrl.split('.').pop()?.toLowerCase();
  return ext === 'jpg' || ext === 'jpeg' || ext === 'png' || ext === 'gif';
}

isVideo(fileUrl: string | undefined): boolean {
  if (!fileUrl) return false;
  const ext = fileUrl.split('.').pop()?.toLowerCase();
  return ext === 'mp4' || ext === 'mov' || ext === 'webm';
}

}
