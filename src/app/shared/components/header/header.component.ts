import { AfterViewInit, Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CategoriesModule } from 'app/modules/categories/categories.module';
import { Popover } from 'bootstrap';

@Component({
  selector: 'app-header',
  standalone: true,
  imports:[CategoriesModule,RouterModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit {

  hovered: string | null = null;
  
  constructor(private router: Router) {
  }

  ngOnInit(): void {
  }
  
  navigateToComponent(menu:string = 'home') {
    if(menu=='list-category') {
      this.router.navigateByUrl('list-category');
    }
  }
  setHover(menu: string | null) {
    this.hovered = menu;
  }
}
