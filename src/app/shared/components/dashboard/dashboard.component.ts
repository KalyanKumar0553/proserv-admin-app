import { Component, OnDestroy, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { ActivatedRoute, NavigationEnd, Router, RouterModule } from '@angular/router';
import { SpinnerComponent } from '../spinner/spinner.component';
import { AuthService } from '../../services/auth.service';
import { HttpClientModule } from '@angular/common/http';
import { NavigationTrackerService } from '../../services/navigration-tracking.service';
import { DeviceDetectorService } from '../../services/device-detector.service';
import { CommonModule, Location } from '@angular/common';
import { BrowserNotSupportedComponent } from '../browser-not-supported/browser-not-supported.component';
import RouteUrl from '../../../shared/constants/router-url.enum';
import { filter, Subscription } from 'rxjs';
import { inject } from '@angular/core';
import { MenuItem } from '../../../shared/models/menu-item.model';
import { appConfig } from '../../../shared/constants/app-config.enum';
import { AppUtilsService } from '../../services/app-utils.service';

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
  isApp:boolean = false;
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

  constructor(private route: ActivatedRoute) { }

  async ngOnInit() {
    this.isMobile = this.deviceDetector.isMobileDevice();
    this.isApp = this.deviceDetector.isApp();
    if (this.isMobile) {
      this.isLoading = false;
    } else {
      this.redirectToLoginOrHome();
      this.appUtils.loadMenuBasedOnRoute(this,this.router.url);
      this.appUtils.updateMenuOnRouteChange(this);
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
