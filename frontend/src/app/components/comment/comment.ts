import { Component, Input, OnInit } from '@angular/core';
import { CommentService,CommentRequest,CommentResponse } from '../../services/comment.service';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../auth/auth.service'; 
import { Post } from '../../services/post.service';

@Component({
  selector: 'app-comment',
  imports: [CommonModule, FormsModule, DatePipe],
  templateUrl: './comment.html',
  styleUrl: './comment.css',
})
export class CommentComponent implements OnInit {
  @Input() postId!: number;      // ID du post pour lequel on affiche les commentaires
  @Input() userId!: number;      // ID de l'utilisateur connecté
  posts : Post[]=[] ;
  comments: CommentResponse[] = [];
  newComment: string = '';
  latestCommentId: number | null = null;

  constructor(private commentService: CommentService, public auth: AuthService) {}

  ngOnInit(): void {
    this.loadComments();
  }

  loadComments() {
    this.commentService.getCommentsByPost(this.postId).subscribe(comments => {
      this.comments = comments;
    });
  }

  addComment() {
    if (!this.newComment.trim()) return;
    if (!this.userId) return console.error('Cannot add comment: userId undefined');
    const request: CommentRequest = {
      postId: this.postId,
      content: this.newComment
    };

    this.commentService.addComment(this.userId, request).subscribe(comment => {
      this.comments.unshift(comment); // Ajout en haut de la liste
      this.newComment = '';  
       this.latestCommentId = comment.id;          // Réinitialiser le champ
         // Supprimer la classe après l'animation (1s ici)
    setTimeout(() => this.latestCommentId = null, 1000);
    });
  }
  deleteComment(commentId: number) {
  this.commentService.deleteComment(commentId, this.userId).subscribe(() => {
    // Supprime le commentaire de la liste sans recharger
    this.comments = this.comments.filter(c => c.id !== commentId);
  }, error => {
    console.error('Impossible de supprimer le commentaire', error);
    alert('Vous n’êtes pas autorisé à supprimer ce commentaire.');
  });
}

}
