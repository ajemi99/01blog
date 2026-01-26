import { Component, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AdminService, PostDto, ReportResponse, UserDto } from '../services/admin.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin-panel',
  imports: [RouterLink,CommonModule],
  templateUrl: './admin-panel.html',
  styleUrl: './admin-panel.css',
})
export class AdminPanel implements OnInit{
  private adminService = inject(AdminService)
  reports : ReportResponse[] =[]
  users : UserDto[] = []
  posts: PostDto[] = [];
  reportPage = 0; reportTotalPages = 0;
  userPage = 0; userTotalPages = 0;
  postPage = 0; postTotalPages = 0;


  ngOnInit() {
    this.loadAllReports(0);
    this.loadUsers(0);
    this.loadPosts(0);
  }

  // 4. Function li kadd-jbed l-data
  loadAllReports(page:number) {
    this.adminService.getAllReports(page,10).subscribe({
      next: (data) => {
        this.reports = data.content; // Dabba l-reports wellaw 3ndna f l-Front!
        this.reportPage = data.page.number;
        this.reportTotalPages = data.page.totalPages;
      },
      error: (err) => console.error("Erreur f l-fetching:", err)
    });
  }


    handleAction(reportId: number, action: string) {
      // 1. Demander une confirmation à l'Admin
      const confirmation = confirm(`Voulez-vous vraiment appliquer l'action : ${action} ?`);
      
      if (confirmation) {
        // 2. Appeler le service pour envoyer la requête au Backend
        this.adminService.takeAction(reportId, action).subscribe({
          next: () => {
            // 3. Si ça marche, on supprime le report de la liste affichée
            // pour que l'Admin voit que le dossier est traité
            this.reports = this.reports.filter(r => r.id !== reportId);
            
            alert("L'action a été effectuée avec succès !");
          },
          error: (err) => {
            console.error("Erreur lors de l'action :", err);
            alert("Une erreur est survenue lors du traitement.");
          }
        });
      }
    }
    
    loadUsers(page:number) {
      this.adminService.getAllUsers(page,10).subscribe({
        next: (data) => {
          this.users = data.content;
          this.userPage = data.page.number;
         this.userTotalPages = data.page.totalPages;
          console.log("Users loaded:", data);
        },
        error: (err) => console.error("Erreur users:", err)
      });
    }

    // Action dial Ban/Unban
    toggleBan(user: any) {
      const action = user.banned ? 'unban' : 'ban';
      if (confirm(`Voulez-vous vraiment ${action} ${user.username} ?`)) {
        const request = user.banned ? this.adminService.unbanUser(user.id) : this.adminService.banUser(user.id);
        
        request.subscribe({
          next: () => {
            user.banned = !user.banned; // Update UI nichan
            alert(`User ${action}ned successfully!`);
          }
        });
      }
    }
    deleteUser(userId: number) {
        // 1. Darouri confirmation hit l-msi7 fih l-khatar (kiy-msa7 posts, comments...)
        if (confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur définitivement ? Cette action est irréversible.')) {
          
          this.adminService.deleteUser(userId).subscribe({
            next: () => {
              // 2. Ila t-msa7 f l-Back, n-7iyidouh mn l-lista f l-Front
              this.users = this.users.filter(u => u.id !== userId);
              alert('Utilisateur supprimé avec succès !');
            },
            error: (err) => {
              console.error("Erreur mlli bghina n-ms7o l-user:", err);
              alert("Erreur lors de la suppression.");
            }
          });
        }
    }
      loadPosts(page :number) {
    this.adminService.getAllPosts(page,10).subscribe({
      next: (data) => {
        this.posts = data.content;
        this.postPage = data.page.number;
        this.postTotalPages = data.page.totalPages;
        console.log("Posts loaded:", data);
      },
      error: (err) => console.error("Erreur posts:", err)
    });
  }
  // 3. Action msa7 post
  onDeletePost(postId: number) {
    if (confirm('Voulez-vous supprimer ce post définitivement ?')) {
      this.adminService.deletePost(postId).subscribe({
        next: () => {
          this.posts = this.posts.filter(p => p.id !== postId);
          alert('Post supprimé !');
        }
      });
    }
  }
  changeUserPage(delta: number) {
    this.loadUsers(this.userPage + delta);
  }
  changePostPage(delta: number) {
    console.log(delta);
    
    this.loadPosts(this.postPage + delta);
  }
  changeReportPage(delta: number) {
    this.loadAllReports(this.reportPage + delta);
  }
}


