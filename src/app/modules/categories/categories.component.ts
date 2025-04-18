import { Component, OnDestroy, OnInit } from '@angular/core';
import { Roles } from 'app/shared/models/roles.enum';
import { AuthService } from 'app/shared/services/auth.service';

@Component({
  selector: 'app-categories',
  standalone: false,
  templateUrl: './categories.component.html',
  styleUrl: './categories.component.scss'
})
export class CategoriesComponent implements OnInit, OnDestroy{
  
  activeLabel:string = 'list-categories';
  roles = Roles;

  ngOnInit(): void {
    this.activeLabel = 'list-categories';
  }

  constructor(public authService: AuthService) {}

  ngOnDestroy(): void {
    this.activeLabel = '';
  }
  
  navItems : any = [
    {
      label: 'Categories',
      expanded: true,
      child:  [
          { label: 'List Categories', route: 'list-categories', active:true },
          { label: 'Add / Update', route: 'add-update-category' },
          { label: 'Delete Categories', route: 'delete-category' },
        ]
    },
  ];

  updateComponent(event:any) {
    this.activeLabel = event;
  }
}
