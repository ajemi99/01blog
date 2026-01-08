import { Component, signal } from '@angular/core';
import { RouterOutlet,Router } from '@angular/router';
import { Navbar } from './components/navbar/navbar'; 
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Navbar, CommonModule],
  templateUrl: './app.html',
  styleUrls: ['./app.css'] // tu avais écrit styleUrl → c'est styleUrls
})
export class App {
  protected readonly title = signal('frontend');
  constructor(private router: Router) {}
  shouldShowNavbar(): boolean {
    // Ma-t-fichich navbar ila kan f login wla register
    const hiddenRoutes = ['/login', '/register', '/'];
    return !hiddenRoutes.includes(this.router.url);
  }
}

