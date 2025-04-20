import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { AppRoutingModule } from 'app/app.routing';
import { ActivatedRoute, NavigationEnd, Router, RouterModule } from '@angular/router';
import { SidenavComponent } from '../sidenav/sidenav.component';
import { SpinnerComponent } from '../spinner/spinner.component';
import { LocalStorageService } from 'app/shared/services/local-service';
import { AuthService } from 'app/shared/services/auth.service';
import { LocalStorageKeys } from 'app/shared/constants/constants.enum';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { NavigationTrackerService } from 'app/shared/services/navigration-tracking.service';
import { DeviceDetectorService } from 'app/shared/services/device-detector.service';
import { CommonModule, Location } from '@angular/common';
import { BrowserNotSupportedComponent } from '../browser-not-supported/browser-not-supported.component';
import RouteUrl from 'app/shared/constants/router-url.enum';
import { ConfigService } from 'app/shared/services/config.service';
import { ComponentConfigService } from 'app/shared/services/component-config.service';
import { filter } from 'rxjs';
import { inject } from '@angular/core';
import { MenuService } from 'app/shared/services/menu.service';
import { MenuItem } from 'app/shared/models/menu-item.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [HeaderComponent, FooterComponent, RouterModule, SpinnerComponent, HttpClientModule, CommonModule, BrowserNotSupportedComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {

  isLoading: boolean = false;
  isMobile: boolean = false;

  private componentConfig = inject(ComponentConfigService);
  private deviceDetector = inject(DeviceDetectorService);
  private router = inject(Router);
  private authService = inject(AuthService);
  private navigationService = inject(NavigationTrackerService);
  private menuService = inject(MenuService);
  private location = inject(Location);

  constructor() { }

  async ngOnInit() {
    this.isMobile = this.deviceDetector.isMobileDevice();
    if (this.isMobile) {
      this.isLoading = false;
    } else {
      this.redirectToLoginOrHome();
      this.loadMenuBasedOnRoute(this.router.url);
      this.updateMenuOnRouteChange();
    }
  }

  updateMenuOnRouteChange() {
    this.router.events
    .pipe(filter(event => event instanceof NavigationEnd))
    .subscribe(() => {
      this.loadMenuBasedOnRoute(this.router.url);
    });
  }
  
  async loadMenuBasedOnRoute(route: string) {
    if(route.length>1) {
      route = route.indexOf("/")==-1 ? route : route.substring(route.indexOf("/")+1);
      if(this.componentConfig.getFileConfig()[route]) {
        let configData = await this.componentConfig.loadConfigBasedOnRoute(route);
        let sideNavMenu = configData.sideNavMenu;
        const state = this.location.getState() as {
          activeLabel : string
        };
        let activeLabel = state?.activeLabel || '';
        this.updateMenuExpansion(sideNavMenu,activeLabel);
        this.menuService.setMenu(sideNavMenu);
      }
    }
  }

  updateMenuExpansion(navItems : MenuItem[],activeLabel: string) {
    let isExpanded = false;
    for(let i=0;i<navItems.length;i++) {
      let currNav = navItems[i];
      currNav.active = false;
      let currNavChild = navItems[i].children;
      for(let j=0;j<currNavChild.length;j++) {
        let currMenuItem = currNavChild[j];
        currMenuItem.active = false;
        if(currMenuItem.route == activeLabel) {
          isExpanded = true;
          currMenuItem.active = true;
          currNav.active = true;
        }
      }
    }
    if((!isExpanded) && navItems.length>0) {
      navItems[0].active = true;
    }
  }

  redirectToLoginOrHome() {
    let previousURL = this.navigationService.getPreviousUrl();
    this.isLoading = true;
    if (previousURL == RouteUrl.LOGIN) {
      this.router.navigate([RouteUrl.HOME]);
      this.isLoading = false;
    } else {
      if (this.authService.getToken() != null) {
        this.authService.ping().then(() => {
          this.router.navigate([RouteUrl.HOME])
        }).catch(err => {
          this.router.navigate([RouteUrl.LOGIN]);
        }).finally(() => {
          this.isLoading = false;
        });
      } else {
        this.isLoading = false;
        this.router.navigate([RouteUrl.LOGIN]);
      }
    }
  }
}
