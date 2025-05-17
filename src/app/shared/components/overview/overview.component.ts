import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import RouteUrl from '../../../shared/constants/router-url.enum';
import { AppUtilsService } from '../../services/app-utils.service';
import { OverviewService } from '../../services/overview.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-overview',
  imports: [CommonModule,RouterModule],
  templateUrl: './overview.component.html',
  styleUrl: './overview.component.scss'
})
export class OverviewComponent implements OnInit,OnDestroy {

  categoriesCount = 0;
  overviewRequestInProgress: boolean = false;
  overviewSubscription: Subscription;

  constructor(private overviewService : OverviewService,private router: Router,private utils:AppUtilsService) {}
  
  ngOnInit(): void {
    this.overviewRequestInProgress = true;
    this.overviewSubscription = this.overviewService.getOverviewData().subscribe((res)=>{
      this.categoriesCount =  res?.statusMsg?.categoriesCount;
      this.overviewRequestInProgress = false;
    },(err)=>{
      this.overviewRequestInProgress = false;
    });
    this.utils.loadMenuBasedOnRoute(this,this.router.url);
    this.utils.updateMenuOnRouteChange(this);
  }

  navigateToComponent(component: string) {
     this.utils.navigateToComponent(this,RouteUrl.CONFIGURATION,component);
  }

  ngOnDestroy(): void {
    this.utils.unsubscribeData([this.overviewSubscription]);
  }
}
