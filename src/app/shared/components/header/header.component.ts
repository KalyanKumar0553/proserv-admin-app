import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, inject, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from 'app/shared/services/auth.service';
import { LocalStorageService } from 'app/shared/services/local-service';
import RouteUrl from 'app/shared/constants/router-url.enum';
import { MenuItem } from 'app/shared/models/menu-item.model';
import { AppUtilsService } from 'app/shared/services/app-utils.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports:[RouterModule,CommonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit,OnChanges {

  authService= inject(AuthService);
  localService = inject(LocalStorageService);
  changeDetector = inject(ChangeDetectorRef);
  appUtils = inject(AppUtilsService);
  hovered: string | null = null;
  activeNav: string = null;
  router = inject(Router);

  @Input()
  sideNavMenu:MenuItem[] = [];

  @Input()
  updateSignal!:number;

  constructor() {}


  ngOnChanges(changes: SimpleChanges) {
    if(changes['updateSignal']) {
        this.changeDetector.detectChanges();
        this.activeNav = this.appUtils.fetchActiveHeader(this.sideNavMenu);
    }
  }

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
    this.authService.logoutUser().subscribe(()=>{
      this.localService.clearData();
      this.router.navigateByUrl(RouteUrl.LOGIN);
    })
  }
}
