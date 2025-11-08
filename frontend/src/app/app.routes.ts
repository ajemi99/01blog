import { Routes } from '@angular/router';
import { Login } from './components/login/login'; 
import { Register } from './components/register/register'; 
import { Home } from './home/home';
import { authGuard } from './auth/auth.guard';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: 'home', component: Home, canActivate: [authGuard] },
  { path: '', pathMatch: 'full', redirectTo: 'home' },
  { path: '**', redirectTo: 'home' }
];
