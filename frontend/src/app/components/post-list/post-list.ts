import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../auth/auth.service';
import { PostService, Post } from '../../services/post.service';

@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.html',
  styleUrls: ['./post-list.css'],
  standalone: true,
  imports: [CommonModule]
})
export class PostListComponent {

  @Input() posts: Post[] = [];
  @Output() editRequested = new EventEmitter<Post>();
  @Output() deleteRequested = new EventEmitter<number>();

  constructor(
    private postService: PostService,
    public auth: AuthService
  ) {}

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
