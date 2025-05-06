import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, inject, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from 'app/shared/services/auth.service';
import { LocalStorageService } from 'app/shared/services/local-service';
import RouteUrl from 'app/shared/constants/router-url.enum';
import { MenuItem } from 'app/shared/models/menu-item.model';
import { AppUtilsService } from 'app/shared/services/app-utils.service';
import { ConfirmationModalComponent } from '../confirmation-modal/confirmation-modal.component';

@Component({
  selector: 'app-header',
  standalone: true,
  imports:[RouterModule,CommonModule,ConfirmationModalComponent],
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

  showLogoutModal:boolean =false;
  deleteRequestInProgress:boolean = false;

  @Input()
  sideNavMenu:MenuItem[] = [];

  @Input()
  updateSignal!:number;

  constructor() {}


  ngOnChanges(changes: SimpleChanges) {
    if(changes['updateSignal']) {
        this.changeDetector.detectChanges();
        if(this.sideNavMenu && this.sideNavMenu.length>0) {
          this.activeNav = this.appUtils.fetchActiveHeader(this.sideNavMenu);
        } else {
          let route = this.router.url;
          route = route.indexOf("/") == -1 ? route : route.substring(route.indexOf("/") + 1);
          this.activeNav = route;
        }
        
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
    this.showLogoutModal = true;
  }

  logoutConfirmation() {
    this.deleteRequestInProgress = true;
    this.authService.logoutUser().toPromise().then(()=>{
      this.localService.clearData();
      this.authService.clearUserData();
      this.router.navigateByUrl(RouteUrl.LOGIN);
      this.showLogoutModal = false;
      this.deleteRequestInProgress = false;
    }).catch(()=>{
      this.showLogoutModal = false;
      this.deleteRequestInProgress = false;
    });
  }
}
