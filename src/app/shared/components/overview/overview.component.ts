import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import RouteUrl from 'app/shared/constants/router-url.enum';
import { OverviewService } from 'app/shared/services/overview.service';

@Component({
  selector: 'app-overview',
  imports: [CommonModule,RouterModule],
  templateUrl: './overview.component.html',
  styleUrl: './overview.component.scss'
})
export class OverviewComponent implements OnInit {

  categoriesCount = 0;
  overviewRequestInProgres: boolean = false;

  constructor(private overviewService : OverviewService,private router: Router) {}
  
  ngOnInit(): void {
    this.overviewRequestInProgres = true;
    this.overviewService.getOverviewData().then(res=>{
        this.categoriesCount =  res?.statusMsg?.categoriesCount;
    }).catch(err=>{
    }).finally(()=>{
      this.overviewRequestInProgres = false;
    });
  }

  navigateToCategories(component: string) {
    this.router.navigate([RouteUrl.CONFIGURATION],{
      state : {
        activeComponent : component
      }
    });
  }
}
