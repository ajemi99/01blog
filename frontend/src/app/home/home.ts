import { Component, OnInit, AfterViewInit, ViewChild, ElementRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth/auth.service';
import { FormsModule } from '@angular/forms';
import { CreatePostComponent } from '../components/create-post/create-post';
import { PostPageResponse, PostResponseDTO, PostService } from '../services/post.service';
import { PostCard } from '../components/post-card/post-card';
import { currentUser, UserService } from '../services/userService';


@Component({
  selector: 'app-home',
  standalone: true,
  imports: [FormsModule, CommonModule, CreatePostComponent,PostCard],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit, AfterViewInit {
  @ViewChild('scrollAnchor') scrollAnchor!: ElementRef;
      posts:PostResponseDTO[] = [];
      currentPage = 0;
      isLastPage = false;
      private postService = inject(PostService)
      private  userService = inject(UserService)
      private observer!: IntersectionObserver;
      currentUser!: currentUser;
      constructor() {}
      
      ngOnInit(): void {
       this.userService.currentUser$.subscribe(user => {
      this.currentUser = user;
      console.log(this.currentUser);
      
    });
    this.loadFeed();
  }
  ngAfterViewInit() {
    this.setupIntersectionObserver();
  }
  setupIntersectionObserver() {
    this.observer = new IntersectionObserver((entries) => {
      // Ila l-user hbaṭ w ban l-Anchor + machi l-page l-lakhera
      if (entries[0].isIntersecting && !this.isLastPage && this.posts.length > 0) {
        console.log('Fetching more posts... 🚀');
        this.loadFeed();
      }
    }, { threshold: 0.1 }); // 10% mn l-element i-bān ghadi i-déclencha

    this.observer.observe(this.scrollAnchor.nativeElement);
  }
    loadFeed() {
      if (this.isLastPage) return;
      this.postService.getFeed(this.currentPage,10).subscribe({
        next: (data:PostPageResponse) => {
          const newPosts = data.content;
          // 🚩 2. Filter: Khli ghir l-posts li l-ID dyalhom machi aslan 3ndna f this.posts
            const uniqueNewPosts = newPosts.filter(newPost => 
              !this.posts.some(existingPost => existingPost.id === newPost.id)
            );
          this.posts = [...this.posts, ...uniqueNewPosts];
          // this.isLastPage = data.last;
          this.isLastPage = (data.page.number + 1) >= data.page.totalPages;
          this.currentPage++;
          console.log('Feed loaded ✅', data);
        },
        error: (err) => console.error('Error loading feed ❌', err)
      });
    }

    onNewPostCreated(post: any) {
      // Kan-qellbou wach had l-post li ja 3ndou ID déjà f l-list
      const index = this.posts.findIndex(p => p.id === post.id);

      if (index !== -1) {
        // Iyeh, hada Edit: Beddel l-post f blastou nichan
        this.posts[index] = post;
      } else {
        // La, hada Create: Zidou l-fouq
        this.posts.unshift(post);
      }
    }
// 🚩 Darouri n-wqfou l-observer mlli n-kharjou mn l-component
    ngOnDestroy() {
      if (this.observer) {
        this.observer.disconnect();
      }
    }
    onCommentsOpened(openedPostId: number) {
      // Dour 3la ga3 l-posts li 3ndek f l-list
      this.posts.forEach(p => {
        if (p.id !== openedPostId) {
          p.showComments = false; // Sedd ay wa7ed machi houwa hada
        }
      });
    }
    onDeleteSuccess(postId: number) {
      // Kan-7iydu l-post men l-array bach i-ghber men l-Front
      this.posts = this.posts.filter(p => p.id !== postId);
    }

}
