import { Location } from '@angular/common';
import { ChangeDetectorRef, Component, inject, Input, OnChanges, OnDestroy, OnInit, SimpleChanges } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { appConfig } from 'app/shared/constants/app-config.enum';
import { MenuItem } from 'app/shared/models/menu-item.model';
import { Roles } from 'app/shared/models/roles.enum';
import { AppUtilsService } from 'app/shared/services/app-utils.service';
import { AuthService } from 'app/shared/services/auth.service';
import { NavigationTrackerService } from 'app/shared/services/navigration-tracking.service';
import { filter, Subscription } from 'rxjs';

@Component({
  selector: 'app-configuration',
  standalone: false,
  templateUrl: './configuration.component.html',
  styleUrl: './configuration.component.scss'
})
export class ConfigurationComponent implements OnInit, OnDestroy {
  
  authService=inject(AuthService);
  changeDetector=inject(ChangeDetectorRef);
  appUtils=inject(AppUtilsService);
  router = inject(Router);
  navigationTrackerService = inject(NavigationTrackerService);
  private routerSubscription!: Subscription;

  activeComponent:string = '';
  roles = Roles;
  location = inject(Location);

  sideNavMenu:MenuItem[] = [];
  updateSignal:number = 0;

  constructor() {}
  
  updateMenuOnRouteChange() {
    this.routerSubscription = this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        const state = this.location.getState() as {
          activeComponent : string
        };
        this.activeComponent = state?.activeComponent ?? 'list-categories';
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

  ngOnInit() {
    this.activeComponent = 'list-categories';
    this.updateMenuOnRouteChange();
    const state = this.location.getState() as {
      activeComponent : string
    };
    this.activeComponent = state?.activeComponent ?? 'list-categories';
    this.loadMenuBasedOnRoute(this.router.url);
  }
  
  componentChangeEvent(activeComponent:string) {
    this.activeComponent = activeComponent;
  }

  ngOnDestroy(): void {
    this.activeComponent = '';
    if(this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
  }
}
