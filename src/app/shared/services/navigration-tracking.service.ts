import { Injectable } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter, pairwise } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class NavigationTrackerService {
  
  private previousUrl: string = '/';

  constructor(private router: Router) {
    this.router.events
      .pipe(
        filter(event => event instanceof NavigationEnd),
        pairwise()
      )
      .subscribe(([prev, current]: [NavigationEnd, NavigationEnd]) => {
        this.previousUrl = prev.urlAfterRedirects;
      });
  }

  public getPreviousUrl(): string {
    return this.previousUrl;
  }
}
