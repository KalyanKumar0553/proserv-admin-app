import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { APP_INITIALIZER, NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { AppRoutingModule } from './app.routing';
import { AppComponent } from './app.component';
import { ConfigurationModule } from './modules/categories/configuration.module';
import { DashboardComponent } from './shared/components/dashboard/dashboard.component';
import { AuthInterceptor } from './shared/services/auth.interceptor';
import { CredentialsInterceptor } from './shared/services/credentials-interceptor';
import { SpinnerComponent } from './shared/components/spinner/spinner.component';
import { ConfigService } from './shared/services/config.service';
import { initializeApp } from './app-init';

@NgModule({
  imports: [
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule,
    ConfigurationModule,
    AppRoutingModule,
    SpinnerComponent
],
  declarations: [
    AppComponent
  ],
  exports:[
    RouterModule,
    AppRoutingModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: CredentialsInterceptor,
      multi: true
    },
    ConfigService,
    {
      provide: APP_INITIALIZER,
      useFactory: initializeApp,
      deps: [ConfigService],
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
