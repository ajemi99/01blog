// profile.ts
import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { currentUser, UserProfileDTO, UserService } from '../../services/userService';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { PostCard } from '../post-card/post-card';
import { HttpClient } from '@angular/common/http';
import { FollowService } from '../../services/followService';
import { PostResponseDTO } from '../../services/post.service';

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
  profileData!: UserProfileDTO;
  posts: PostResponseDTO[] = [];
  isLoading = true;
  isProcessing = false;
  currentUser!:currentUser;
  currentPage = 0;
   isLastPage = false;
  ngOnInit() {
// 🚩 Had s-sâr houwa l-mohim: kiy-7ḍi ay tebdila f l-URL
  this.route.paramMap.subscribe(params => {
    const newUsername = params.get('username');
    if (newUsername) {
      this.resetAndFetch(newUsername);
    }
  });
       this.userService.currentUser$.subscribe(user => {
      this.currentUser = user;
      console.log(this.currentUser);
      
    });
    
  }
  resetAndFetch(username: string) {
    // 1. Khwi d-data d l-user l-qdim
    this.posts = [];
    this.currentPage = 0;
    this.isLastPage = false;
    
    // 2. 3iyyet l l-API b s-smiya l-jdida
    this.fetchProfile(username); 
  }

  fetchProfile(username: string) {
    if(this.isLastPage)return;
    // 🚩 Ngoulou isLoading = true ghir ila kante l-page 0
    if (this.currentPage === 0) this.isLoading = true;
    this.userService.getUserProfile(username,this.currentPage,10).subscribe({
      next: (data:UserProfileDTO) => {
        this.profileData = data;
                  const newPosts = data.posts.content;
          // 🚩 2. Filter: Khli ghir l-posts li l-ID dyalhom machi aslan 3ndna f this.posts
        const uniqueNewPosts = newPosts.filter(newPost => 
             !this.posts.some(existingPost => existingPost.id === newPost.id)
        );
        this.posts = [...this.posts,...uniqueNewPosts]
        console.log(this.profileData);
        this.isLastPage = (this.profileData.posts.page.number + 1) >= this.profileData.posts.page.totalPages;
        this.currentPage++;
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
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
    this.posts.forEach((p: any) => {
      if (p.id !== postId) p.showComments = false;
    });
  }

  // Mlli kadd-delete post men l-card
  onPostDeleted(postId: number) {
    this.posts = this.posts.filter((p: any) => p.id !== postId);
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
