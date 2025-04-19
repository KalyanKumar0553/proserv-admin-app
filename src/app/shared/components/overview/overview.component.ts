import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { OverviewService } from 'app/shared/services/overview.service';

@Component({
  selector: 'app-overview',
  imports: [CommonModule],
  templateUrl: './overview.component.html',
  styleUrl: './overview.component.scss'
})
export class OverviewComponent implements OnInit {

  categoriesCount = 0;
  overviewRequestInProgres: boolean = false;

  constructor(private overviewService : OverviewService) {}
  
  ngOnInit(): void {
    this.overviewRequestInProgres = true;
    this.overviewService.getOverviewData().then(res=>{
        this.categoriesCount =  res?.statusMsg?.categoriesCount;
    }).catch(err=>{

    }).finally(()=>{
      this.overviewRequestInProgres = false;
    });
  }
}
