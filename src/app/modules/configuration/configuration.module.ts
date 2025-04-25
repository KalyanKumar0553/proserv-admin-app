import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConfigurationRoutingModule } from './configuration-routing.module';
import { ListCategoryComponent } from './list-category/list-category.component';
import { ConfigurationComponent } from './configuration.component';
import { SidenavComponent } from 'app/shared/components/sidenav/sidenav.component';
import { DeleteCategoryComponent } from './delete-category/delete-category.component';
import { AddUpdateCategoryComponent } from './add-update-category/add-update-category.component';
import { UnauthorizedComponent } from 'app/shared/components/unauthorized/unauthorized.component';
import { SettingsComponent } from './settings/settings.component';


@NgModule({
  declarations: [
    ListCategoryComponent,
    ConfigurationComponent,
    DeleteCategoryComponent,
    AddUpdateCategoryComponent,
    SettingsComponent
  ],
  imports: [
    CommonModule,
    ConfigurationRoutingModule,
    SidenavComponent,
    UnauthorizedComponent
  ]
})
export class ConfigurationModule { }
