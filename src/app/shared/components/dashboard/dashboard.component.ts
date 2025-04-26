import { Component, OnDestroy, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { NavigationEnd, Router, RouterModule } from '@angular/router';
import { SpinnerComponent } from '../spinner/spinner.component';
import { AuthService } from 'app/shared/services/auth.service';
import { HttpClientModule } from '@angular/common/http';
import { NavigationTrackerService } from 'app/shared/services/navigration-tracking.service';
import { DeviceDetectorService } from 'app/shared/services/device-detector.service';
import { CommonModule, Location } from '@angular/common';
import { BrowserNotSupportedComponent } from '../browser-not-supported/browser-not-supported.component';
import RouteUrl from 'app/shared/constants/router-url.enum';
import { filter, Subscription } from 'rxjs';
import { inject } from '@angular/core';
import { MenuItem } from 'app/shared/models/menu-item.model';
import { appConfig } from 'app/shared/constants/app-config.enum';
import { AppUtilsService } from 'app/shared/services/app-utils.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [HeaderComponent, FooterComponent, RouterModule, SpinnerComponent, HttpClientModule, CommonModule, BrowserNotSupportedComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit,OnDestroy {

  isLoading: boolean = false;
  isMobile: boolean = false;

  private appUtils = inject(AppUtilsService);
  private deviceDetector = inject(DeviceDetectorService);
  private router = inject(Router);
  private authService = inject(AuthService);
  private navigationService = inject(NavigationTrackerService);
  private sideNavMenu: MenuItem[] = [];
  private routerSubscription!: Subscription;
  private location = inject(Location);
  private updateSignal: number = 0;
  private activeComponent: string = '';

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
    this.routerSubscription = this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        const state = this.location.getState() as {
          activeComponent : string
        };
        this.activeComponent = state?.activeComponent || '';
        this.loadMenuBasedOnRoute(this.router.url);
      });
  }

  loadMenuBasedOnRoute(route: string) {
    if (route.length > 1) {
      route = route.indexOf("/") == -1 ? route : route.substring(route.indexOf("/") + 1);
      if (appConfig[route]) {
        let configData = appConfig[route];
        let navItems = configData.sideNavMenu;
        this.appUtils.updateMenuExpansion(navItems, this.activeComponent);
        this.sideNavMenu = navItems;
        this.updateSignal++;
      }
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
        this.authService.ping().subscribe(() => {
          this.router.navigate([RouteUrl.HOME])
          this.isLoading = false;
        }),(err => {
          this.router.navigate([RouteUrl.LOGIN]);
          this.isLoading = false;
        });
      } else {
        this.isLoading = false;
        this.router.navigate([RouteUrl.LOGIN]);
      }
    }
  }

  ngOnDestroy(): void {
      if(this.routerSubscription) {
        this.routerSubscription.unsubscribe();
      }
  }
}
