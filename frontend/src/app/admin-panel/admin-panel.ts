import { Component, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AdminService, PostDto, ReportResponse, UserDto } from '../services/admin.service';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
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
  private route = inject(ActivatedRoute);
  searchTerm: string = '';

  ngOnInit() {
    this.loadAllReports();
    this.loadUsers();
    this.loadPosts();
    // 📡 T-ssant l l-URL: kollma tbeddel s-search f navbar, hadchi kiy-khdem
    this.route.queryParams.subscribe(params => {
      this.searchTerm = params['search']?.toLowerCase() || '';
    });
  }
  // 🔍 Getter bach n-filteriw l-reports f l-blassa
get filteredReports() {
  if (!this.searchTerm) return this.reports;
  
  const term = this.searchTerm.toLowerCase();
  
  return this.reports.filter(r => {
    // 1. Check f l-usernames w l-reason
    const matchText = r.reason?.toLowerCase().includes(term) || 
                      r.reportedUsername?.toLowerCase().includes(term) ||
                      r.reporterUsername?.toLowerCase().includes(term);

    // 2. Check f l-Post ID (ila kān moujoud)
    // Kan-7wlou r.postId l-string bach n-qrawh 
    const matchPostId = r.postId ? r.postId.toString().includes(term) : false;

    return matchText || matchPostId;
  });
}
  // 🔍 Getter bach n-filteriw l-users
  get filteredUsers() {
    if (!this.searchTerm) return this.users;
    return this.users.filter(u => 
      u.username?.toLowerCase().includes(this.searchTerm) || 
      u.email?.toLowerCase().includes(this.searchTerm)
    );
  }
    get filteredPosts() {
    if (!this.searchTerm) return this.posts;
    const term = this.searchTerm.toLowerCase();
    
    return this.posts.filter(p => 
      p.author?.toLowerCase().includes(term) || 
      p.description?.toLowerCase().includes(term) ||
      p.id.toString().includes(term) // Search b l-Post ID nichan
    );
  }
  // 4. Function li kadd-jbed l-data
  loadAllReports() {
    this.adminService.getAllReports().subscribe({
      next: (data) => {
        this.reports = data; // Dabba l-reports wellaw 3ndna f l-Front!
        console.log("Reports loaded:", data);
      },
      error: (err) => console.error("Erreur f l-fetching:", err)
    });
  }
  // N'oublie pas d'ajouter l'import de AdminService si ce n'est pas déjà fait
// private adminService = inject(AdminService);

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

    loadUsers() {
      this.adminService.getAllUsers().subscribe({
        next: (data) => {
          this.users = data;
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
      loadPosts() {
    this.adminService.getAllPosts().subscribe({
      next: (data) => {
        this.posts = data;
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
}


