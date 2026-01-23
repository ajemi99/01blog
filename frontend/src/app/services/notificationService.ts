import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';

export interface NotificationDTO {
  id: number;
  message: string;
  read: boolean;
  createdAt: string;
}
export interface notificationsPageResponse {
    content:NotificationDTO[];
    page: {
        size: number;
        number: number;      // Page l-7aliya
        totalElements: number;
        totalPages: number;   // Total dial l-pages
      };
}

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private apiUrl = 'http://localhost:8080/api/notifications';
  
  // Subject bach n-updatiw l-badge (dak l-3adad l-7mer) f l-waqt
  private unreadCountSubject = new BehaviorSubject<number>(0);
  unreadCount$ = this.unreadCountSubject.asObservable();

  constructor(private http: HttpClient) {}

  // 1. Get ga3 notifications
getNotifications(page: number): Observable<notificationsPageResponse> {
  // Sift page query param l-Backend
  return this.http.get<notificationsPageResponse>(`${this.apiUrl}?page=${page}&size=10`);
}

  // 2. Get unread count
  refreshUnreadCount(): void {
    this.http.get<number>(`${this.apiUrl}/unread-count`).subscribe(count => {
        
      this.unreadCountSubject.next(count);
    });
  }

  // 3. Mark as read
  markAsRead(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}/read`, {}).pipe(
      tap(() => this.refreshUnreadCount()) // N-n9so l-count mlli t-qra
    );
  }
    markAllAsRead(): Observable<void> {
        return this.http.put<void>(`${this.apiUrl}/read-all`, {}).pipe(
            tap(() => this.refreshUnreadCount()) // Bach r-rqem f l-badge i-welli 0
        );
    }
}