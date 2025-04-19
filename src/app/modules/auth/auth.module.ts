import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppLoginComponent } from './app-login/app-login.component';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from 'app/app.routing';
import { RouterModule } from '@angular/router';
import { SpinnerComponent } from 'app/shared/components/spinner/spinner.component';
import { DeviceDetectorService } from 'app/shared/services/device-detector.service';
import { BrowserNotSupportedComponent } from 'app/shared/components/browser-not-supported/browser-not-supported.component';


@NgModule({
  declarations: [
    AppLoginComponent,
  ],
  imports: [
    CommonModule,
    BrowserModule, 
    FormsModule,
    AppRoutingModule,
    ReactiveFormsModule,
    SpinnerComponent,
    BrowserNotSupportedComponent
  ],
  exports :[
    RouterModule,
    AppRoutingModule
  ]
})
export class AuthModule { }
