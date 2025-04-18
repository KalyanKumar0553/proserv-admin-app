import { NgModule } from '@angular/core';
import { CommonModule, } from '@angular/common';
import { BrowserModule  } from '@angular/platform-browser';
import { Routes, RouterModule } from '@angular/router';
import RouteUrl from './shared/constants/router-url.enum';
import { AppLoginComponent } from './modules/auth/app-login/app-login.component';
import { DashboardComponent } from './shared/components/dashboard/dashboard.component';
import { ListCategoryComponent } from './modules/categories/list-category/list-category.component';
import { OverviewComponent } from './shared/components/overview/overview.component';
import { CategoriesComponent } from './modules/categories/categories.component';
import { RoleGuard } from './shared/services/role-guard';
import { Roles } from './shared/models/roles.enum';
import { UnauthorizedComponent } from './shared/components/unauthorized/unauthorized.component';

const routes: Routes =[
  {
    path: '',
    component: DashboardComponent,
    children: [
      {
        path: 'categories',
        component: CategoriesComponent,
        canActivate: [RoleGuard],
        data: { roles: [Roles.ADMIN,Roles.USER] },
      },
      {
        path: 'home',
        component: OverviewComponent,
        canActivate: [RoleGuard],
        data: { roles: [Roles.ADMIN,Roles.USER] },
      },
    ]
  },
  {
    path: 'login',
    component: AppLoginComponent,
  },
  {
    path: 'unauthorized',
    component: UnauthorizedComponent,
  },
];

@NgModule({
  imports: [
    CommonModule,
    BrowserModule,
    RouterModule.forRoot(routes,{
       useHash: true
    })
  ],
  exports: [
  ],
})
export class AppRoutingModule { }
