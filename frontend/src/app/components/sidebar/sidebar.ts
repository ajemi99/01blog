import { Component, inject, OnInit,OnDestroy } from '@angular/core';
// import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class Sidebar implements OnInit, OnDestroy {
 public user: any;
  private authService = inject(AuthService)
  private userSub!: Subscription;
  constructor() {}

  ngOnInit() {
  this.userSub = this.authService.currentUser$.subscribe(userData => {
      this.user = userData;
    });
  }
  ngOnDestroy() {
    // Darouri n-7eydou l-ishti-rak mlli n-seddou l-page bach may-kounch "Memory Leak"
    if (this.userSub) {
      this.userSub.unsubscribe();
    }
  }
}
