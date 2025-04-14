import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppLoginComponent } from './app-login/app-login.component';
import { ComponentsModule } from 'app/shared/components/components.module';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    AppLoginComponent,
  ],
  imports: [
    CommonModule,
    ComponentsModule,
    BrowserModule, 
    FormsModule
  ]
})
export class AuthModule { }
