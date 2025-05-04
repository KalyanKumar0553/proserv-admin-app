import { Location } from '@angular/common';
import { ChangeDetectorRef, Component, inject, Input, OnChanges, OnDestroy, OnInit, SimpleChanges } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
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
  categoryID:string = '';
  private routerSubscription!: Subscription;

  activeComponent:string = '';
  roles = Roles;
  location = inject(Location);

  sideNavMenu:MenuItem[] = [];
  updateSignal:number = 0;

  constructor() {}
  
  ngOnInit() {
    this.activeComponent = 'list-categories';
    this.appUtils.updateMenuOnRouteChange(this);
    const state = this.location.getState() as {
      activeComponent : string
    };
    this.activeComponent = state?.activeComponent ?? 'list-categories';
    this.appUtils.loadMenuBasedOnRoute(this,this.router.url);
  }

  updateComponent(componentData: any) {
    let component = componentData?.component || '';
    this.categoryID = componentData?.state?.id;
    if(component) {
      this.activeComponent = component;
      this.appUtils.loadMenuBasedOnRoute(this,this.router.url);
    }
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
