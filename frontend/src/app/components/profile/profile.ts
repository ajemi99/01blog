import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { UserService } from '../../services/userService';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-profile',
  templateUrl: './profile.html',
  styleUrl: './profile.css',
  imports: [RouterLink,FormsModule, CommonModule],
})
export class Profile implements OnInit {
  private route = inject(ActivatedRoute); // Bach n-jbdou l-URL
  private userService = inject(UserService);

  profileData: any; // Hna ghadi t-khabba l-data jaya men l-backend
  isLoading = true;

  ngOnInit() {
    // 1. "Listen" l l-URL: kollma tbeddel s-miya f l-barre d'adresse, khdem
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
        console.log(data);
        
        this.profileData = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error(err);
        this.isLoading = false;
      }
    });
  }
}