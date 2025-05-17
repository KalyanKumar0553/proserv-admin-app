import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BreadCrumbItem } from '../../../shared/models/bread-crumb-item.model';
import { AppUtilsService } from '../../../shared/services/app-utils.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-breadcrumb',
  imports: [CommonModule,FormsModule],
  templateUrl: './breadcrumb.component.html',
  styleUrl: './breadcrumb.component.scss'
})
export class BreadCrumbComponent {
  @Input()
  crumbItems ?: BreadCrumbItem[];

  constructor(private utils:AppUtilsService,private router:Router){}

  navigateToComponent(route: string,component:string) {
    this.utils.navigateToComponent(this,route,component);
 }
}
