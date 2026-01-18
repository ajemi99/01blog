import { Component, inject, signal } from '@angular/core';
import { RouterOutlet,Router } from '@angular/router';
import { Navbar } from './components/navbar/navbar'; 
import { CommonModule } from '@angular/common';
import { CreatePostComponent } from './components/create-post/create-post';
import { Sidebar } from './components/sidebar/sidebar';
import { AuthService } from './services/auth/auth.service';
import { FollowService } from './services/followService';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Navbar, CommonModule,CreatePostComponent,Sidebar],
  templateUrl: './app.html',
  styleUrls: ['./app.css'] // tu avais écrit styleUrl → c'est styleUrls
})
export class App {
  private authService = inject(AuthService);
  private followService = inject(FollowService)
  protected readonly title = signal('frontend');
  private followSub?: Subscription;
  constructor(public router: Router) {} // Khlliha public bach t-sta3melha f HTML

  ngOnInit() {
    // 1. Kan-chekiw wach l-token kayn f LocalStorage s7i7
    if (this.authService.isAuthenticated()) {
      // 2. 3ad n-chargiw l-user
      this.fetchSidbarData()
     this.followSub= this.followService.followStatus$.subscribe(() => {
         this.fetchSidbarData()// Kat-3awed t-jbed l-counts l-jdad
    });
    }
  }
  fetchSidbarData(){
          this.authService.loadCurrentUser().subscribe({
        error: (err) => {
          console.error('Initial load failed', err);
          // Ila l-token khrbeqtih b yeddik, hada ghadi i-kharjek nichan
          if (err.status === 403 || err.status === 401 || err.status === 500) {
            this.authService.logout();
          }
        }
      });
  }
  // ngOnDestroy() {
  //   if (this.followSub) {
  //     this.followSub.unsubscribe(); // 🛑 9té3 l-khit!
  //     console.log("Sidebar subscription closed safely.");
  //   }
  // }
  shouldShowNavbar(): boolean {
    const hiddenRoutes = ['/login', '/register'];
    return !hiddenRoutes.includes(this.router.url);
  }
  
  shouldShowSidbar():boolean {
    const hiddenRoutes = ['/login', '/register','/notifications'];
    const currentUrl = this.router.url;
     if (hiddenRoutes.includes(currentUrl)) {
      return false;
    }
    if (currentUrl.startsWith('/profile')) {
      return false;
    }
    if (currentUrl.startsWith('/admin-panel')){
      return false;
    }

     return true;
  }
}

