import { Component, Input, Output, EventEmitter, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { commentPageResponse, CommentResponse, CommentService } from '../../services/comment.service';
import { PostResponseDTO, PostService } from '../../services/post.service';
import { LikeService } from '../../services/LikeService';
import { RouterLink } from '@angular/router';
import { UserService } from '../../services/userService';

@Component({
  selector: 'app-post-card',
  imports: [CommonModule, FormsModule,RouterLink],
  templateUrl: './post-card.html',
  standalone: true,
  styleUrl: './post-card.css',
})
export class PostCard {
  @Input() post: any;        // L-post li ghadi i-ji men l-Home aw Profile
  @Input() currentUser: any; // Bach n-عرفو isOwner dial l-comment masalan
  @Input() editComponent?: any;
  private comments:CommentResponse[]=[];
  private currentCommentPage:number=0;
   isLastCommentPage:boolean = false;
   userService = inject(UserService)
  constructor(private postService: PostService,
    private commentService: CommentService,
    private likeService: LikeService,
  ){}
   @Output() postDeleted = new EventEmitter<number>();
   @Output() commentsOpened = new EventEmitter<number>();
  get authenticatedUser() {
    return this.userService.currentUserValue;
  }
  onDeletePost() {
    if (confirm('Voulez-vous vraiment supprimer ce post ?')) {
      this.postService.deletePost(this.post.id).subscribe({
        next: () => {
          // Mlli kiy-tmsa7 f l-Backend, kanclemiw l-Parent
          this.postDeleted.emit(this.post.id);
        },
        error: (err) => console.error("Erreur de suppression", err)
      });
    }
  }
  onEditClick() {
  if (this.editComponent) {
    // Kat-3ayat nichan l-function li wast CreatePost bach t-7el l-modal
    this.editComponent.openEditMode(this.post);
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

      onLike(post: any) {
      // 1. Ila kante aslan isLiking kheddama, 7bess
      if (post.isLiking) return;

      // 2. Creer l-field f l-blassa w diro true
      post.isLiking = true;

      this.likeService.toggleLike(post.id).subscribe({
        next: (res: any) => {
          // res jay fih { "message": "liked" }
          if (res.status === 'liked') {
            post.liked = true;
            post.likesCount++;
          } else if (res.status === 'unliked') {
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

 onToggleComments(currentPost: PostResponseDTO) {
  
    if (currentPost.showComments) {
      currentPost.showComments = false;
      return;
    }

    this.commentsOpened.emit(this.post.id);
    currentPost.showComments = true;

    // شحن الصفحة الأولى فقط إذا كانت القائمة فارغة
    if (!currentPost.comments || currentPost.comments.length === 0) {
      this.currentCommentPage = 0; // إعادة التصفير عند أول فتح
      this.isLastCommentPage = false;
      this.loadMoreComments(currentPost);
    }
  }
  loadMoreComments(currentPost: any) {
    if (this.isLastCommentPage || currentPost.isLoadingComments) return;

    currentPost.isLoadingComments = true;
      if (!currentPost.comments) {
        currentPost.comments = [];
      }
    this.commentService.getCommentsByPost(currentPost.id, this.currentCommentPage, 10).subscribe({
      next: (res: commentPageResponse) => {
        const newComments = res.content;
        const uniqueNewComments = newComments.filter(newComment => 
              !currentPost.comments.some((existingComment:any) => existingComment.id === newComment.id)
            );
        // دمج التعليقات الجديدة مع القديمة
        currentPost.comments = [...(currentPost.comments || []), ...uniqueNewComments];
        
        // تحديث حالة الصفحة
        // Spring Pageable كيعطيك معلومات ف res.page (أو res مباشرة حسب الـ JSON اللي كيصيفط السيرفر)
        // إذا كان السيرفر كيصيفط Page object كامل:
        this.isLastCommentPage = (res.page.number + 1) >= res.page.totalPages;
        this.currentCommentPage++;
        
        currentPost.isLoadingComments = false;
      },
      error: () => currentPost.isLoadingComments = false
    });
  }


  onAddComment() {
    if (!this.post.newCommentText || !this.post.newCommentText.trim()) return;

    const request = {
      postId: this.post.id,
      content: this.post.newCommentText
    };

    this.commentService.addComment(request).subscribe({
      next: (newComment) => {
        if (!this.post.comments) this.post.comments = [];
        // Zid l-comment jdid l-fouq
        this.post.comments.unshift(newComment);
        this.post.newCommentText = ''; // Khwi l-input
        // Ila 3ndek commentCount f l-post, tqder t-zidou
        if(this.post.commentCount !== undefined) this.post.commentCount++;
      }
    });
  }
    onDeleteComment( commentId: number) {
    if (confirm('Voulez-vous supprimer ce commentaire ?')) {
      this.commentService.deleteComment(commentId).subscribe({
        next: () => {
          this.post.comments = this.post.comments.filter((c: any) => c.id !== commentId);
          if(this.post.commentCount > 0) this.post.commentCount--;
        }
      });
    }
  }


// Report Variables
  selectedPostId: number | null = null;
  reportReason: string = '';
  isReporting: boolean = false;

  openReportModal(postId: number) {
    console.log("Opening Modal for ID:", postId);
    this.selectedPostId = postId;
    this.reportReason = ''; 
  }

  submitReport() {
    if (!this.selectedPostId || !this.reportReason.trim()) return;

    this.isReporting = true;
    this.userService.reportPost(this.selectedPostId, this.reportReason).subscribe({
      next: (res) => {
        console.log("Report Success:", res);
        // alert('Report sent successfully');
        
        this.isReporting = false;
        this.reportReason = '';

        // 🚀 Dynamic Close
        const closeBtn = document.getElementById('closeReportModal' + this.post.id);
        if (closeBtn) closeBtn.click();
      },
      error: (err) => {
        console.error("Report Error:", err);
        alert(err.error?.message || 'Error sending report');
        this.isReporting = false;
      }
    });
  }

  closeModal() {
    this.selectedPostId = null;
    this.reportReason = '';
  }

}
