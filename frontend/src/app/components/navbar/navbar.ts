import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {
  constructor(private router: Router) {}

  onLogout() {
  localStorage.removeItem('token');
//  this.userSubject.next(null); // Khwi l-khzana dial RAM
  window.location.href = '/login';
  }
  openCreatePost() {
    console.log("Modal for creating a post opened!");
    // Hna ghadi n-zidou logic dial Bootstrap Modal f l-khatwa l-jaya
  }
}
