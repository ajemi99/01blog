import { Component, EventEmitter, Output, Input, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common'; 
import { PostService } from '../../services/post.service';

@Component({
  selector: 'app-create-post',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './create-post.html',
  styleUrls: ['./create-post.css']
})
export class CreatePostComponent implements OnInit {
  @Output() postCreated = new EventEmitter<any>();
  
  // Zdna had l-ID bach n-3erfou chmen post gha n-beddlou
  postId: number | null = null; 
  mode: 'create' | 'edit' = 'create';

  description: string = '';
  file: File | null = null;
  imagePreview: string | null = null;

  constructor(private postService: PostService) {}

  ngOnInit() {}

  // --- HADI HIYA L-MOUKKH: l-fonction li ghadi n-3eytou liha f Edit ---
  openEditMode(post: any) {
    this.mode = 'edit';
    this.postId = post.id;
    this.description = post.description;
    
    // Kat-chouf wach l-post fih mediaUrl bach t-tal3ha f l-preview
    this.imagePreview = post.mediaUrl ? 'http://localhost:8080' + post.mediaUrl : null;

    // Kat-7el l-modal (Bootstrap 5 logic)
    const modalElement = document.getElementById('createPostModal');
    if (modalElement) {
      // @ts-ignore (Bach TypeScript may-t-chekkach 3la bootstrap)
      const modal = new (window as any).bootstrap.Modal(modalElement);
      modal.show();
    }
  }

  onFileSelected(event: any) {
    const selectedFile = event.target.files[0];
    if (selectedFile) {
      this.file = selectedFile;
      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result as string;
      };
      reader.readAsDataURL(selectedFile);
    }
  }

  removeImage() {
    this.file = null;
    this.imagePreview = null;
    const fileInput = document.getElementById('fileInput') as HTMLInputElement;
    if (fileInput) fileInput.value = '';
  }

  submitPost() {
    if (!this.description.trim() && !this.file) return;

    if (this.mode === 'create') {
      this.postService.createPost(this.description, this.file ?? undefined).subscribe({
        next: (newPost) => {
          this.postCreated.emit(newPost);
          this.resetForm();
        }
      });
    } else {
      // EDIT MODE: Kan-siftou l-postId li khzenna mlli t-7ellat l-modal
      this.postService.updatePost(this.postId!, this.description, this.file ?? undefined).subscribe({
        next: (updatedPost) => {
          this.postCreated.emit(updatedPost);
          this.resetForm();
        },
        error: (err) => console.error('Update error:', err)
      });
    }
  }

  resetForm() {
    this.description = '';
    this.file = null;
    this.imagePreview = null;
    this.mode = 'create'; // Dima rjje3ha create mlli t-sali
    this.postId = null;
  }
}