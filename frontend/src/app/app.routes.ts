import { Routes } from '@angular/router';
import { Login } from './components/login/login'; 
import { Register } from './components/register/register'; 
import { Home } from './home/home';
import { authGuard } from './services/auth/auth.guard';
import { MyPostsComponent } from './components/my-posts/my-posts';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: 'home', component: Home, canActivate: [authGuard] },
  { path: 'my-posts', component: MyPostsComponent },
  { path: '', pathMatch: 'full', redirectTo: 'home' },
  { path: '**', redirectTo: 'home' },

];
