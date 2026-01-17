import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { UserService } from '../../services/userService';
import { debounceTime, distinctUntilChanged, Observable, Subject, switchMap } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth/auth.service';
import { FollowService } from '../../services/followService';
import { HttpClient } from '@angular/common/http';
import { NotificationService } from '../../services/notificationService';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
@Component({
  selector: 'app-navbar',
  imports: [RouterLink,FormsModule, CommonModule,InfiniteScrollModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar implements OnInit {
  //  private apiUrl = 'http://localhost:8080/api/notifications';
  searchTerm: string = '';
  searchResults: any[] = [];
  showDropdown: boolean = false;
  unreadCount:any;
  private userService = inject(UserService)
  private followService = inject(FollowService)
  private searchSubject = new Subject<string>();
  private notificationService = inject(NotificationService)
  currentUser: any;
  constructor(private router: Router,private authService: AuthService, private http:HttpClient) {
    this.authService.currentUser$.subscribe(user => {
    this.currentUser = user;
    console.log(this.currentUser);
  });
  
    this.searchSubject.pipe(
      debounceTime(300), // Tsennah i-kmml l-ktiba (300ms)
      distinctUntilChanged(), // Ila l-kelma ma-t-beddlatch, mat-siftch requête
      switchMap(term => {
        // if (term.trim().length < 2) return [[]]; // Ila kteb ghir 7erf, khwi l-list
        return this.userService.searchUsers(term);
      })
    ).subscribe({
      next: (data) => {
        console.log(data);
        
        this.searchResults = data;
        this.showDropdown = data.length > 0;
      },
      error: (err) => console.error("Search error", err)
    });
  }
  ngOnInit() {
  this.notificationService.unreadCount$.subscribe(count => {
    console.log(count);
    
    this.unreadCount = count;
  });
  if (this.authService.isAuthenticated()) {
    this.notificationService.refreshUnreadCount();
  } // Loadih f l-lowel
}

  onLogout() {
    localStorage.removeItem('token');
    this.followService.notifyFollowUpdate();
    window.location.href = '/login';
  }

  onSearchChange() {
    // Ila msa7 l-ktiba b-merra
    if (this.searchTerm.trim().length === 0) {
      this.searchResults = [];
      this.showDropdown = false;
      return;
    }
    
    this.searchSubject.next(this.searchTerm);
  }

    // Bach t-ghber l-list mlli i-cliqui b-ra
    hideDropdown() {
      setTimeout(() => this.showDropdown = false, 300);
    }
    selectUser() {
    this.searchTerm = '';        // Khwi l-ktiba li f l-input
    this.searchResults = [];     // Khwi l-list dial l-users li tla3at
    this.showDropdown = false;   // Sedd l-menu dial l-ba7t
  }
  // Variables l-asasiya
page = 0;
notifications: any[] = [];
isLoading = false;
hasMore = true;

// Mlli ycliqui 3la l-bell, loadi l-page 0
onBellClick() {
  if (this.notifications.length === 0) {
    this.loadMoreNotifications();
  }
}

loadMoreNotifications() {
  if (this.isLoading || !this.hasMore) return;

  this.isLoading = true;
  this.notificationService.getNotifications(this.page).subscribe(res => {
    this.notifications = [...this.notifications, ...res.content];
    // this.hasMore = !res.last;
    this.page++;
    this.isLoading = false;
  });
}
onNotifClick(notif: any) {
    console.log('Notification clicked:', notif);
    // Hna tqder d-dir l-logic dial markAsRead
  }
    markAsRead(notif: any) {
    if (notif.read) return;
    
    this.notificationService.markAsRead(notif.id).subscribe(() => {
      notif.read = true; // Update UI nichan
    });
  }
    markAllAsRead() {
    this.notificationService.markAllAsRead().subscribe({
      next: () => {
        // 1. Update l-UI nichan bla ma n-tsennaw refresh
        this.notifications.forEach(n => n.read = true);
        
        // 2. Radd l-unreadCount l 0
        this.unreadCount = 0;
        
        console.log('All notifications marked as read');
      },
      error: (err) => console.error('Error marking all as read', err)
    });
  }


}
