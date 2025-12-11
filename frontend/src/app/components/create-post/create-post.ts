
import { Component, EventEmitter, Output, Input, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
// import { HttpClientModule, HttpClient } from '@angular/common/http';
import { PostService } from '../../services/post.service';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.html',
  styleUrls: ['./create-post.css'],
  standalone: true,
  imports: [FormsModule]
})
export class CreatePostComponent implements OnInit  {
  @Output() postCreated = new EventEmitter<any>();
  @Input()  mode: 'create' | 'edit' = 'create';
  @Input() postData: any = null;

  description: string = '';
  file!: File | null;

  constructor(private postService: PostService) {}

  ngOnInit() {
    if (this.mode === 'edit' && this.postData) {
      this.description = this.postData.description || '';
      // Note: file cannot be prefilled, user can select new file
    }
  }
  onFileSelected(event: any) {
    this.file = event.target.files[0];
  }

  submitPost() {

  if (!this.description.trim() && !this.file) {
    alert("مايمكنش تسيفط بوست خاوي، خاص description ولا صورة !");
    return;
  }
  if(this.mode ==='create'){
    this.postService.createPost(this.description,this.file?? undefined).subscribe({
      next: (newPost) => {
        
        // alert('Post created successfully!');
        this.description = '';
        this.file = null;
        this.postCreated.emit(newPost);
      },
      error: (err) => {
        console.error(err);
        // alert('Error creating post');
      }
    });

  }else{
    this.postService.updatePost(this.postData.id, this.description, this.file ?? undefined).subscribe({
    next: (updatedPost) => {
      console.log(updatedPost.updatedAt);
      
      this.description = '';
      this.file = null;
      this.postCreated.emit(updatedPost); // emit same as create
    },
    error: (err) => console.error(err)
    });
  }
  }
}
