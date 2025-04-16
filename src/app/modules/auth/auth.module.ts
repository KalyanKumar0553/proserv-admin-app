import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppLoginComponent } from './app-login/app-login.component';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from 'app/app.routing';
import { RouterModule } from '@angular/router';



@NgModule({
  declarations: [
    AppLoginComponent,
  ],
  imports: [
    CommonModule,
    BrowserModule, 
    FormsModule,
    AppRoutingModule
  ],
  exports :[
    RouterModule,
    AppRoutingModule
  ]
})
export class AuthModule { }
