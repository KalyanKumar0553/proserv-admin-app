import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpInterceptor,
  HttpHandler,
  HttpRequest,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { LocalStorageKeys } from '../constants/constants.enum';
import { LocalStorageService } from './local-service';
import { AuthService } from './auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  
  constructor(private router: Router,private localService:LocalStorageService,private authService:AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    
    const authReq = this.authService.getToken()
      ? req.clone({
          headers: req.headers.set('Authorization', `Bearer ${this.authService.getToken()}`)
        })
      : req;

    return next.handle(authReq).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          this.localService.clearData();
          this.router.navigate(['/login']);
        }
        return throwError(() => error);
      })
    );
  }
}