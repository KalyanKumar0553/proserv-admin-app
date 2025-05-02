import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConfigurationRoutingModule } from './configuration-routing.module';
import { ListCategoryComponent } from './list-category/list-category.component';
import { ConfigurationComponent } from './configuration.component';
import { SidenavComponent } from 'app/shared/components/sidenav/sidenav.component';
import { AddUpdateCategoryComponent } from './add-update-category/add-update-category.component';
import { UnauthorizedComponent } from 'app/shared/components/unauthorized/unauthorized.component';
import { SettingsComponent } from './settings/settings.component';
import { DataGridComponent } from 'app/shared/components/data-grid/data-grid.component';
import { BreadCrumbComponent } from 'app/shared/components/breadcrumb/breadcrumb.component';
import { ConfirmationModalComponent } from 'app/shared/components/confirmation-modal/confirmation-modal.component';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { DataAccordionComponent } from 'app/shared/components/data-accordion/data-accordion.component';
import { MatTabsModule } from '@angular/material/tabs';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ReactiveFormsModule } from '@angular/forms';
import { AddUpdateCategoryTaskComponent } from './add-update-category-task/add-update-category-task.component';

@NgModule({
  declarations: [
    ListCategoryComponent,
    ConfigurationComponent,
    AddUpdateCategoryComponent,
    SettingsComponent,
    AddUpdateCategoryTaskComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    BrowserModule,
    ConfigurationRoutingModule,
    SidenavComponent,
    UnauthorizedComponent,
    DataGridComponent,
    BreadCrumbComponent,
    ConfirmationModalComponent,
    DataAccordionComponent,
    MatTabsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    ReactiveFormsModule

  ]
})
export class ConfigurationModule { }
