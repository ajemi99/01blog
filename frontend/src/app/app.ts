import { Component, inject, signal } from '@angular/core';
import { RouterOutlet,Router } from '@angular/router';
import { Navbar } from './components/navbar/navbar'; 
import { CommonModule } from '@angular/common';
import { CreatePostComponent } from './components/create-post/create-post';
import { Sidebar } from './components/sidebar/sidebar';
import { AuthService } from './services/auth/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Navbar, CommonModule,CreatePostComponent,Sidebar],
  templateUrl: './app.html',
  styleUrls: ['./app.css'] // tu avais écrit styleUrl → c'est styleUrls
})
export class App {
  private authService = inject(AuthService);
  protected readonly title = signal('frontend');
  
  constructor(public router: Router) {} // Khlliha public bach t-sta3melha f HTML

  ngOnInit() {
    // 1. Kan-chekiw wach l-token kayn f LocalStorage s7i7
    if (this.authService.isAuthenticated()) {
      // 2. 3ad n-chargiw l-user
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
  }

  shouldShowNavbar(): boolean {
    const hiddenRoutes = ['/login', '/register', '/'];
    return !hiddenRoutes.includes(this.router.url);
  }
}

