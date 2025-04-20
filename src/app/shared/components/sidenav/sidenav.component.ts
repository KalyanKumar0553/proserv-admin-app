import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, EventEmitter, inject, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { ComponentLoaderService } from 'app/shared/services/compinent-loader.service';
import { MenuService } from 'app/shared/services/menu.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-sidenav',
  standalone: true,
  imports:[CommonModule],
  templateUrl: './sidenav.component.html',
  styleUrl: './sidenav.component.scss'
})
export class SidenavComponent implements OnInit {
  
  menuService = inject(MenuService);
  componentLoaderService = inject(ComponentLoaderService);

  navMenu = [];

  constructor(private changeDetecion : ChangeDetectorRef){
  }
  
  ngOnInit(): void {
    this.menuService.menuItems$.subscribe(data => {
      this.navMenu = data;
    })
  }

  toggleExpansion(navItem:any= {}) {
    this.navMenu.forEach(element => {
      if(navItem.label!=element.label) {
        element.active = false;
      }
    });
    navItem.active = !navItem.active;
    this.changeDetecion.detectChanges();
  }

  setActiveLabel(navItem:any={}) {
    let prevActiveRoute = '';
    this.navMenu.forEach(element => {
      element.children.forEach(item=>{
        if(item.active) {
          prevActiveRoute = item.route;
        }
        item.active=false;
      });
    });
    navItem.active=true;
    if(navItem.route != prevActiveRoute) {
      this.componentLoaderService.setComponent(navItem.route);
    }
  }
}
