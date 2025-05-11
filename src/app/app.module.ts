import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { APP_INITIALIZER, NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { AppRoutingModule } from './app.routing';
import { AppComponent } from './app.component';
import { ConfigurationModule } from './modules/configuration/configuration.module';
import { DashboardComponent } from './shared/components/dashboard/dashboard.component';
import { AuthInterceptor } from './shared/services/auth.interceptor';
import { CredentialsInterceptor } from './shared/services/credentials-interceptor';
import { SpinnerComponent } from './shared/components/spinner/spinner.component';
import { ConfigService } from './shared/services/config.service';
import { initializeApp } from './app-init';
import { SnackbarComponent } from './shared/components/snackbar/snackbar.component';
import { TokenExpiryInterceptor } from './shared/services/token-expiry.interceptor';

@NgModule({
  imports: [
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule,
    ConfigurationModule,
    AppRoutingModule,
    SpinnerComponent,
    SnackbarComponent
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
      useClass: TokenExpiryInterceptor,
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
