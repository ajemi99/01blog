import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { UserService } from '../../services/userService';
import { debounceTime, distinctUntilChanged, Subject, switchMap } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink,FormsModule, CommonModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {
  searchTerm: string = '';
  searchResults: any[] = [];
  showDropdown: boolean = false;
  private userService = inject(UserService)
  private searchSubject = new Subject<string>();
  currentUser: any;
  constructor(private router: Router,private authService: AuthService) {
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

  onLogout() {
    localStorage.removeItem('token');
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
}
