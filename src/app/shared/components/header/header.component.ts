import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CategoriesModule } from 'app/modules/categories/categories.module';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MenuService } from 'app/shared/services/menu.service';
import { AuthService } from 'app/shared/services/auth.service';
import { LocalStorageService } from 'app/shared/services/local-service';
import RouteUrl from 'app/shared/constants/router-url.enum';

@Component({
  selector: 'app-header',
  standalone: true,
  imports:[CategoriesModule,RouterModule,CommonModule,MatToolbarModule,MatIconModule,MatMenuModule,MatButtonModule,MatSidenavModule,MatListModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit {

  menuService = inject(MenuService);
  authService= inject(AuthService);
  localService = inject(LocalStorageService);

  hovered: string | null = null;
  activeNav: string = null;
  router = inject(Router);


  constructor() {}

  ngOnInit(): void {
    this.activeNav = 'home';
  }
 
  setHover(menu: string | null) {
    this.hovered = menu;
  }

  isHovered(menu: string | null) {
    return this.hovered == menu;
  }

  setActive(nav: string) {
    this.activeNav = nav;
  }
  
  isActive(nav: string): boolean {
    return this.hovered == null && this.activeNav === nav;
  }

  logout() {
    this.authService.logoutUser().then(()=>{
      this.localService.clearData();
      this.router.navigateByUrl(RouteUrl.LOGIN);
    })
  }
}
