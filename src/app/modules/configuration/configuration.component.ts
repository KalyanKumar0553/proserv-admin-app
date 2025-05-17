import { Location } from '@angular/common';
import { ChangeDetectorRef, Component, inject, Input, OnChanges, OnDestroy, OnInit, SimpleChanges } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { MenuItem } from '../../shared/models/menu-item.model';
import { Roles } from '../../shared/models/roles.enum';
import { AppUtilsService } from '../../shared/services/app-utils.service';
import { AuthService } from '../../shared/services/auth.service';
import { NavigationTrackerService } from '../../shared/services/navigration-tracking.service';
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

  constructor(private route: ActivatedRoute) {}
  
  ngOnInit() {
    this.activeComponent = 'list-categories';
    this.appUtils.updateMenuOnRouteChange(this);
    this.route.queryParams.subscribe(params => {
      const activeComponent = params['activeComponent'];
      if (activeComponent) {
        this.activeComponent = activeComponent ?? 'list-categories';
        this.appUtils.loadMenuBasedOnRoute(this,this.router.url);
      }
    });
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
    if(activeComponent.label && activeComponent.route && (activeComponent.label == activeComponent.route)) {
      this.appUtils.navigateToComponent(this,activeComponent.route,activeComponent.label);
    } else {
      this.activeComponent = activeComponent.route;
    }
  }

  ngOnDestroy(): void {
    this.activeComponent = '';
    if(this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
  }
}
