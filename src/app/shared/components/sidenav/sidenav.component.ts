import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, EventEmitter, inject, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { Router } from '@angular/router';
import RouteUrl from 'app/shared/constants/router-url.enum';
import { MenuItem } from 'app/shared/models/menu-item.model';
import { AppUtilsService } from 'app/shared/services/app-utils.service';

@Component({
  selector: 'app-sidenav',
  standalone: true,
  imports:[CommonModule],
  templateUrl: './sidenav.component.html',
  styleUrl: './sidenav.component.scss'
})
export class SidenavComponent implements OnInit,OnChanges {
  
  @Input()
  sideNavMenu:MenuItem[] = [];

  @Input()
  updateSignal!:number;

  @Output() componentChangeEvent = new EventEmitter<string>();

  private router = inject(Router);

  constructor(private changeDetector : ChangeDetectorRef,private appUtils:AppUtilsService){
  }
  
  ngOnInit(): void {}

  ngOnChanges(changes: SimpleChanges) {
    if(changes['updateSignal']) {
      this.changeDetector.detectChanges();
    }
  }

  toggleExpansion(navItem:any= {}) {
    this.sideNavMenu.forEach(element => {
      if(navItem.label!=element.label) {
        element.active = false;
      }
    });
    navItem.active = !navItem.active;
    this.changeDetector.detectChanges();
  }

  setActiveLabel(navItem:any={}) {
    let prevActiveRoute = '';
    this.sideNavMenu.forEach(element => {
      element.children.forEach(item=>{
        if(item.active) {
          prevActiveRoute = item.route;
        }
        item.active=false;
      });
    });
    navItem.active=true;
    if(navItem.route != prevActiveRoute) {
      this.componentChangeEvent.emit(navItem.route);
    }
  }
}
