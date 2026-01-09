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
  @Input() mode: 'create' | 'edit' = 'create';
  @Input() postData: any = null;

  description: string = '';
  file: File | null = null;
  imagePreview: string | null = null;

  constructor(private postService: PostService) {}

  ngOnInit() {
    if (this.mode === 'edit' && this.postData) {
      this.description = this.postData.description || '';
      if (this.postData.imageUrl) {
        this.imagePreview = this.postData.imageUrl;
      }
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
    // Reset l-input bach t-qder t-khtar nfs tswira ila bghiti
    const fileInput = document.getElementById('fileInput') as HTMLInputElement;
    if (fileInput) fileInput.value = '';
  }

  submitPost() {
    if (!this.description.trim() && !this.file) {
      alert("Please add some text or an image!");
      return;
    }

    if (this.mode === 'create') {
      this.postService.createPost(this.description, this.file ?? undefined).subscribe({
        next: (newPost) => {
          this.resetForm();
          this.postCreated.emit(newPost);
        },
        error: (err) => console.error('Error:', err)
      });
    } else {
      this.postService.updatePost(this.postData.id, this.description, this.file ?? undefined).subscribe({
        next: (updatedPost) => {
          this.resetForm();
          this.postCreated.emit(updatedPost);
        },
        error: (err) => console.error('Error:', err)
      });
    }
  }

  resetForm() {
    this.description = '';
    this.file = null;
    this.imagePreview = null;
  }
}