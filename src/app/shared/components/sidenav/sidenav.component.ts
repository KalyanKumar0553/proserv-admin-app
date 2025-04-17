import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-sidenav',
  standalone: true,
  imports:[CommonModule],
  templateUrl: './sidenav.component.html',
  styleUrl: './sidenav.component.scss'
})
export class SidenavComponent {
  
  @Input()
  navItems = [];

  @Output() componentChangeEvent = new EventEmitter<string>();

  constructor(private changeDetecion : ChangeDetectorRef){
  }

  toggleExpansion(navItem:any= {}) {
    this.navItems.forEach(element => {
      if(navItem.label!=element.label) {
        element.expanded = false;
      }
    });
    navItem.expanded = !navItem.expanded;
    this.changeDetecion.detectChanges();
  }

  setActiveLabel(navItem:any={}) {
    let prevActiveRoute = '';
    this.navItems.forEach(element => {
      element.child.forEach(item=>{
        if(item.active) {
          prevActiveRoute = item.route;
        }
        item.active=false;
      });
    });
    navItem.active=true;
    if(navItem.route != prevActiveRoute) {
      console.log(navItem.route);
      console.log(prevActiveRoute);
      this.componentChangeEvent.emit(navItem.route);
    }
  }
}
