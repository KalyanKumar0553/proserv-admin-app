import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpResponse } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { TokenExpiryService } from './token-expiry.service';

@Injectable()
export class TokenExpiryInterceptor implements HttpInterceptor {

  constructor(private expiryService: TokenExpiryService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      tap(event => {
        if (event instanceof HttpResponse) {
          const expiresAt = event.headers.get('X-Token-Expires-At');
          if (expiresAt) {
            this.expiryService.scheduleExpiryPopup(expiresAt);
          }
        }
      })
    );
  }
}