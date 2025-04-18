import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CategoriesRoutingModule } from './categories-routing.module';
import { ListCategoryComponent } from './list-category/list-category.component';
import { CategoriesComponent } from './categories.component';
import { SidenavComponent } from 'app/shared/components/sidenav/sidenav.component';
import { DeleteCategoryComponent } from './delete-category/delete-category.component';
import { AddUpdateCategoryComponent } from './add-update-category/add-update-category.component';
import { UnauthorizedComponent } from 'app/shared/components/unauthorized/unauthorized.component';


@NgModule({
  declarations: [
    ListCategoryComponent,
    CategoriesComponent,
    DeleteCategoryComponent,
    AddUpdateCategoryComponent
  ],
  imports: [
    CommonModule,
    CategoriesRoutingModule,
    SidenavComponent,
    UnauthorizedComponent
  ]
})
export class CategoriesModule { }
