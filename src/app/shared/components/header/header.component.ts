import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, inject, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { LocalStorageService } from '../../services/local-service';
import RouteUrl from '../../../shared/constants/router-url.enum';
import { MenuItem } from '../../../shared/models/menu-item.model';
import { AppUtilsService } from '../../services/app-utils.service';
import { ConfirmationModalComponent } from '../confirmation-modal/confirmation-modal.component';
import { Subscription } from 'rxjs';
import { Roles } from '../../../shared/models/roles.enum';
declare const bootstrap: any;
import { Location } from '@angular/common';
import { SidenavMobileComponent } from '../sidenav-mobile/sidenav-mobile.component';
@Component({
  selector: 'app-header',
  standalone: true,
  imports:[RouterModule,CommonModule,ConfirmationModalComponent,SidenavMobileComponent],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit,OnChanges {

  categoryID:string = '';
  private routerSubscription!: Subscription;

  activeComponent:string = '';
  roles = Roles;
  location = inject(Location);

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
  
  defaultMenu: MenuItem[] = [
    {
      "label": "home",
      "labelText": "Home",
      "active": true,
      "route": "/"
    },
    {
        "label": "configuration",
        "labelText": "Configuration",
        "active": false,
        "route": "/configuration"
    }
  ];

  constructor(private route: ActivatedRoute) {}

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

  setHover(menu: string | null) {
    this.hovered = menu;
  }

  isHovered(menu: string | null) {
    return this.hovered == menu;
  }

  setActive(nav: string,route:string) {
    this.activeNav = nav;
    this.appUtils.navigateToComponent(this,nav,route);
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

  toggleOffcanvas(): void {
    const offcanvasEl = document.getElementById('offcanvasExample');
    if (offcanvasEl) {
      const bsOffcanvas = bootstrap.Offcanvas.getOrCreateInstance(offcanvasEl);
      bsOffcanvas.toggle();
    }
  }

  ngOnInit() {
    this.activeNav = 'home';
    this.activeComponent = 'home';
    this.appUtils.updateMenuOnRouteChange(this);
    const state = this.location.getState() as {
      activeComponent : string
    };
    this.appUtils.loadMenuBasedOnRoute(this,this.router.url);
    if(this.sideNavMenu.length == 0) {
        this.sideNavMenu = this.defaultMenu;
    }
  }

  updateComponent(componentData: any) {
    let component = componentData?.component || '';
    this.categoryID = componentData?.state?.id || null;
    if(component) {
      this.activeComponent = component;
      this.appUtils.loadMenuBasedOnRoute(this,this.router.url);
    }
  }
  
  componentChangeEvent(activeComponent:MenuItem) {
    this.categoryID = null;
    let activeHeader = this.appUtils.fetchActiveHeader(this.sideNavMenu);
    if(activeComponent.label == activeComponent.route) {
      this.appUtils.navigateToComponent(this,activeComponent.label,activeComponent.route);
    } else {
      this.appUtils.navigateToComponent(this,activeHeader,activeComponent.route);
    }
    
    this.activeComponent = activeComponent.route;
    if(!(activeComponent?.children?.length)) {
      this.toggleOffcanvas();
    }
  }

  ngOnDestroy(): void {
    this.activeComponent = '';
    if(this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
  }
}
