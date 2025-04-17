import { Component, OnDestroy, OnInit } from '@angular/core';

@Component({
  selector: 'app-categories',
  standalone: false,
  templateUrl: './categories.component.html',
  styleUrl: './categories.component.scss'
})
export class CategoriesComponent implements OnInit, OnDestroy{
  
  activeLabel:string = 'list-categories';
  
  ngOnInit(): void {
    this.activeLabel = 'list-categories';
  }

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
