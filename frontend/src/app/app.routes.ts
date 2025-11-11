import { Routes } from '@angular/router';
import { Login } from './components/login/login'; 
import { Register } from './components/register/register'; 
import { Home } from './home/home';
import { authGuard } from './auth/auth.guard';
import { CreatePost } from './components/create-post/create-post';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: 'home', component: Home, canActivate: [authGuard] },
  { path:'create-post', component:CreatePost },
  { path: '', pathMatch: 'full', redirectTo: 'home' },
  { path: '**', redirectTo: 'home' }
];
