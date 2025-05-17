import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppLoginComponent } from './app-login/app-login.component';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { SpinnerComponent } from '../../shared/components/spinner/spinner.component';
import { DeviceDetectorService } from '../../shared/services/device-detector.service';
import { BrowserNotSupportedComponent } from '../../shared/components/browser-not-supported/browser-not-supported.component';
import { AppRoutingModule } from 'app/app.routing';
import { FooterComponent } from 'app/shared/components/footer/footer.component';


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
    BrowserNotSupportedComponent,
    FooterComponent
  ],
  exports :[
    RouterModule,
    AppRoutingModule
  ]
})
export class AuthModule { }
