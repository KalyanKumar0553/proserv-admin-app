import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import RouteUrl from 'app/shared/constants/router-url.enum';
import { BreadCrumbItem } from 'app/shared/models/bread-crumb-item.model';
import { AppUtilsService } from 'app/shared/services/app-utils.service';

@Component({
  selector: 'app-breadcrumb',
  imports: [CommonModule,FormsModule],
  templateUrl: './breadcrumb.component.html',
  styleUrl: './breadcrumb.component.scss'
})
export class BreadCrumbComponent {
  @Input()
  crumbItems : BreadCrumbItem[];

  constructor(private utils:AppUtilsService){}

  navigateToComponent(route: string,component:string) {
    this.utils.navigateToComponent(route,component);
 }
}
