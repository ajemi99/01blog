import { Routes } from '@angular/router';
import { Login } from './components/login/login'; 
import { Register } from './components/register/register'; 
import { Home } from './home/home';
import { authGuard } from './services/auth/auth.guard';
import { Profile } from './components/profile/profile';
import { AdminPanel } from './admin-panel/admin-panel';
import { NotFound } from './components/not-found/not-found';
import { adminGuard } from './services/auth/admin.guard';

export const routes: Routes = [
  { path: 'login', component: Login, data: { hideLayout: true } },
  { path: 'register', component: Register, data: { hideLayout: true } },

  { path: 'home', component: Home, canActivate: [authGuard] },
  { path: 'profile/:username', component: Profile, data: { hideSidebar: true } },
  { path: 'admin-panel', component: AdminPanel,canActivate: [adminGuard], data: { hideSidebar: true } },

  { path: '', pathMatch: 'full', redirectTo: 'home' },

  // ✅ NotFound بلا Navbar و Sidebar
  { path: '**', component: NotFound, data: { hideLayout: true } }
];
