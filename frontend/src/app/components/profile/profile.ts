// profile.ts
import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../../services/userService';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { PostCard } from '../post-card/post-card';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.html',
  imports: [FormsModule, CommonModule,PostCard],
  styleUrl: './profile.css'
})
export class Profile implements OnInit {
  private route = inject(ActivatedRoute);
  private userService = inject(UserService);
  
  profileData: any;
  isLoading = true;

  ngOnInit() {
    // Listen l-username f l-URL
    this.route.paramMap.subscribe(params => {
      const username = params.get('username');
      if (username) {
        this.fetchProfile(username);
      }
    });
  }

  fetchProfile(username: string) {
    this.isLoading = true;
    this.userService.getUserProfile(username).subscribe({
      next: (data) => {
        this.profileData = data;
        this.isLoading = false;
      }
    });
  }

  // Had l-function kat-khdem mlli kadd-cliqui 3la chi post akhor
  onCommentsOpened(postId: number) {
    this.profileData.posts.forEach((p: any) => {
      if (p.id !== postId) p.showComments = false;
    });
  }

  // Mlli kadd-delete post men l-card
  onPostDeleted(postId: number) {
    this.profileData.posts = this.profileData.posts.filter((p: any) => p.id !== postId);
    this.profileData.postsCount--;
  }
}
