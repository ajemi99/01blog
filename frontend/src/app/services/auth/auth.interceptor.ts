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
            // 1. Check wach l-mouchkil machi f l-auth (login/register)
            // T-akked ana l-path fih '/login' awla '/auth' 3la 7asab l-Backend dyalk
            const isAuthRequest = req.url.includes('/login') || req.url.includes('/auth');

            // 2. Ila 401/403 w MACHI auth request (ya3ni chi khtat f l-app), 3ad n-kharjouh
            if ((error.status === 401 || error.status === 403) && !isAuthRequest) {
                console.warn('Invalid Token! Redirecting...');
                localStorage.removeItem('token');
                window.location.href = '/login'; 
            }
            
            // 3. DAROURI: rjje3 l-error (throwError) bach t-wesal l-Login Component
            return throwError(() => error);
        })
    );
}
}