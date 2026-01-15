import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class FollowService {
  // Had l-Subject kiy-sift l-khbar l ga3 l-components
  private followStatusChanged = new Subject<void>();
  
  // Had l-Observable kiy-tsennawh l-components (Sidebar)
  followStatus$ = this.followStatusChanged.asObservable();

  // Function bach n-3lmo l-nas kamlin
  notifyFollowUpdate() {
    this.followStatusChanged.next();
  }
}