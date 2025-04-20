import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { MenuItem } from 'app/shared/models/menu-item.model';
import { Roles } from 'app/shared/models/roles.enum';
import { AuthService } from 'app/shared/services/auth.service';
import { ComponentLoaderService } from 'app/shared/services/compinent-loader.service';
import { MenuService } from 'app/shared/services/menu.service';

@Component({
  selector: 'app-categories',
  standalone: false,
  templateUrl: './categories.component.html',
  styleUrl: './categories.component.scss'
})
export class CategoriesComponent implements OnInit, OnDestroy{
  
  public authService=inject(AuthService);
  private componentLoaderService=inject(ComponentLoaderService);
  private menuService=inject(MenuService);

  activeLabel:string = 'list-categories';
  roles = Roles;
  menuItems: MenuItem[] = [];

  constructor() {}

  async ngOnInit() {
    this.menuService.menuItems$.subscribe(menu=>{
      menu.forEach(m=>{
        m.children.forEach(c=>{
          if(c.active) {
            this.activeLabel = c.route;
          }
        })
      });
    })
    this.componentLoaderService.component$.subscribe(component=>{
      this.activeLabel = component || 'list-categories';
    });
  }

  ngOnDestroy(): void {
    this.activeLabel = '';
  }
}
