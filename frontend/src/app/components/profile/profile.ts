// profile.ts
import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/userService';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { PostCard } from '../post-card/post-card';
import { HttpClient } from '@angular/common/http';
import { FollowService } from '../../services/followService';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.html',
  imports: [FormsModule, CommonModule,PostCard],
  styleUrl: './profile.css'
})
export class Profile implements OnInit {
  private route = inject(ActivatedRoute);
  private userService = inject(UserService);
  private followService = inject(FollowService)
  constructor(private http: HttpClient,private router :Router){}
  profileData: any;
  isLoading = true;
  isProcessing = false;

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
        console.log(this.profileData);
        
        this.isLoading = false;
      },
      error: (err) => {
      if (err.status === 404) {
        // 🚩 Ila user makaynach, siftou l-page 404 nichan
        console.warn('User not found, redirecting to 404...');
        this.router.navigate(['/404'], { skipLocationChange: true });
      }
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
  onFollowToggle() {
    if (this.isProcessing) return;
    this.isProcessing = true;
    
    const url = `http://localhost:8080/api/follow/${this.profileData.id}`;

    // 1. Beddel l-type l <any> awla n-حدد l-interface
    this.http.post<any>(url, {}).subscribe({
      next: (res) => {
        console.log("Response from backend:", res);
        
        // 2. Jbed l-boolean men wast l-object res.following
        const isNowFollowing = res.following; 

        this.profileData.following = isNowFollowing;
        
        // 3. Update count 3la 7sab l-boolean l-7aqiqi
        if (isNowFollowing) {
          this.profileData.followersCount++;
        } else {
          this.profileData.followersCount--;
        }
        this.followService.notifyFollowUpdate();
        this.isProcessing = false;
      },
      error: (err) => {
        console.error("Error", err);
        this.isProcessing = false;
      }
    });
  }
}
