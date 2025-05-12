import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, EventEmitter, inject, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { Router } from '@angular/router';
import RouteUrl from '../../constants/router-url.enum';
import { MenuItem } from '../../models/menu-item.model';
import { AppUtilsService } from '../../services/app-utils.service';

@Component({
  selector: 'app-sidenav-mobile',
  standalone: true,
  imports:[CommonModule],
  templateUrl: './sidenav-mobile.component.html',
  styleUrl: './sidenav-mobile.component.scss'
})
export class SidenavMobileComponent implements OnInit,OnChanges {
  
  @Input()
  sideNavMenu:MenuItem[] = [];

  @Input()
  updateSignal!:number;

  @Output() componentChangeEvent = new EventEmitter<MenuItem>();

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
      if(navItem.labelText!=element.labelText) {
        element.active = false;
      }
    });
    navItem.active = !navItem.active;
    this.changeDetector.detectChanges();
  }

  setActiveLabel(navItem:MenuItem) {
    let prevActiveComponent = this.appUtils.fetchActiveComponentFromMenu(this.sideNavMenu);
    this.sideNavMenu.forEach(element => {
      element.active = false;
      (element?.children || []).forEach(item=>{
        if(item.route == navItem.route) {
          element.active = true;
          item.active = true;
        } else {
          item.active=false;
        }
      });
    });
    navItem.active=true;
    if(navItem.route != prevActiveComponent) {
      this.componentChangeEvent.emit(navItem);
    }
  }
}
