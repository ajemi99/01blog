import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  
  constructor(private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('token');
    let authReq = req;

    if (token) {
        authReq = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
    }

    return next.handle(authReq).pipe(
catchError((error: HttpErrorResponse) => {  
    console.log('Backend Error Body:', error.error); // 🔍 Checki hada f l-console

    const isAuthRequest = req.url.includes('/login') || req.url.includes('/auth');

    if ((error.status === 401 || error.status === 403) && !isAuthRequest) {
        let reason = 'expired';
        
        // Jbed l-message mn l-body
        const backendMessage = error.error?.message || '';
        console.log(backendMessage);
        

        if (backendMessage.includes('not found') || backendMessage.includes('deleted')) {
            reason = 'deleted';
        } else if (backendMessage.includes('banned') || backendMessage.includes('suspended')) {
            console.log(1111111111111111111111111111111111111111111111111111);
            
            reason = 'banned';
        }

        localStorage.removeItem('token');
        this.router.navigate(['/login'], { queryParams: { reason: reason } });
    }
    return throwError(() => error);
})
    );
  }
}