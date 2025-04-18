// src/app/guards/role.guard.ts
import { Injectable } from '@angular/core';
import {
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  Router
} from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Roles } from '../models/roles.enum';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {

  constructor(private auth: AuthService, private router: Router) {}

  private hasAccess(route: ActivatedRouteSnapshot): boolean {
    const expectedRoles: Roles[] = route.data['roles'] || route.parent?.data['roles'] || [];
    const userRoles = this.auth.getRoles();
    return userRoles.some(role => expectedRoles.includes(role));
  }
  
  canActivate(route: ActivatedRouteSnapshot): boolean {
    return this.hasAccess(route) || (this.router.navigate(['/unauthorized']), false);
  }
  
  canActivateChild(childRoute: ActivatedRouteSnapshot): boolean {
    return this.hasAccess(childRoute) || (this.router.navigate(['/unauthorized']), false);
  }
}
