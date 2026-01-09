import { Component, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../auth/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink,CommonModule],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class Sidebar implements OnInit {
  user: any;
  private authService = inject(AuthService)
  constructor() {}

  ngOnInit() {
    this.authService.getCurrentUser().subscribe({
      next: (data) => {

        console.log(data);
        this.user = data;
        
      },
      error: (err) => console.error('Error fetching profile', err)
    });
  }
}
