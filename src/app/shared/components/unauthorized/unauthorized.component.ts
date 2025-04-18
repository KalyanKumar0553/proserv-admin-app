import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { NavigationTrackerService } from 'app/shared/services/navigration-tracking.service';

@Component({
  selector: 'app-unauthorized',
  imports: [],
  templateUrl: './unauthorized.component.html',
  styleUrl: './unauthorized.component.scss'
})
export class UnauthorizedComponent {

  @Input()
  hideHome: boolean = false;

  constructor(private router: Router,private navigationTracker: NavigationTrackerService) {}

  goHome() {
    let previousUrl = this.navigationTracker.getPreviousUrl();
    previousUrl = (previousUrl == '/' || previousUrl == '/unauthorized')  ? '/login' : previousUrl;
    console.log(previousUrl);
    this.router.navigateByUrl(previousUrl);
  }
}
