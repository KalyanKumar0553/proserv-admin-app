import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { AppRoutingModule } from './app.routing';
import { AppComponent } from './app.component';
import { CategoriesModule } from './modules/categories/categories.module';
import { DashboardComponent } from './shared/components/dashboard/dashboard.component';
import { AuthInterceptor } from './shared/services/auth.interceptor';
import { CredentialsInterceptor } from './shared/services/credentials-interceptor';
import { SpinnerComponent } from './shared/components/spinner/spinner.component';

@NgModule({
  imports: [
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule,
    CategoriesModule,
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
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
