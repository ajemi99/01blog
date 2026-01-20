import { inject } from "@angular/core";
import { UserService } from "../userService";
import { CanActivateFn, Router } from "@angular/router";
import { filter, map, take } from "rxjs";


export const adminGuard: CanActivateFn = () => {
  const userService = inject(UserService);
  const router = inject(Router);

  // Ghadi n-tsennaw l-userSubject 7ta may-bqach null
  return userService.currentUser$.pipe(
    // 1. Ila kan null (baqi kiy-loadi), n-tsennaw l-valeur l-jaya
    filter(user => user !== null), 
    take(1), // Gher t-ji l-valeur l-lowla s7i7a, 7bess l-stream
    map(user => {
      if (user.role === 'ADMIN') {
        return true;
      } else {
        router.navigate(['/home']);
        return false;
      }
    })
  );
};