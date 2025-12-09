import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostService, Post } from '../../services/post.service';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.html',
  styleUrls: ['./post-list.css'],
  standalone: true,
  imports: [CommonModule]
})
export class PostListComponent {
  posts: Post[] = [];

  constructor(private postService: PostService,public auth: AuthService) {
    this.loadPosts();

  }

  loadPosts() {
    this.postService.getPosts().subscribe({
      next: (data) => {
        this.posts = data.map(p=>({...p,showMenu: false}))
        console.log(this.posts);
        
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
toggleMenu(post: any) {
  post.showMenu = !post.showMenu;
}
editPost(post: Post) {
  console.log("Edit:", post);
}

deletePost(id: number) {
  if (!confirm("Vous voulez vraiment supprimer ce post ?")) return;

  this.postService.deletePost(id).subscribe({
    next: () => {
      // نحيدو البوست من اللائحة بلا ما نعاودو نجيبو من API
      this.posts = this.posts.filter(p => p.id !== id);
    },
    error: (err) => console.error(err)
  });
}

}
