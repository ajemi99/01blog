import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  
  constructor(private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('token'); // Awla 'this.tokenKey' dyalk
    let authReq = req;

    if (token) {
      authReq = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
    }

// auth.interceptor.ts
return next.handle(authReq).pipe(
  catchError((error: HttpErrorResponse) => {
    // Check wach l-mouchkil machi f l-login endpoint
    const isLoginRequest = req.url.includes('/login');

    if ((error.status === 401 || error.status === 403) && !isLoginRequest) {
      console.warn('Invalid Token! Clearing session...');
      localStorage.removeItem('token');
      window.location.href = '/login'; 
    }
    
    // Khlli l-error t-douz bach l-Login Component i-qder i-choufha w i-t-afficha
    return throwError(() => error);
  })
);
  }
}