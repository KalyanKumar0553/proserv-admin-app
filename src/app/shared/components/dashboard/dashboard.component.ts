import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { AppRoutingModule } from 'app/app.routing';
import { Router, RouterModule } from '@angular/router';
import { SidenavComponent } from '../sidenav/sidenav.component';
import { SpinnerComponent } from '../spinner/spinner.component';
import { LocalStorageService } from 'app/shared/services/local-service';
import { AuthService } from 'app/shared/services/auth.service';
import { LocalStorageKeys } from 'app/shared/constants/constants.enum';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { NavigationTrackerService } from 'app/shared/services/navigration-tracking.service';
import { DeviceDetectorService } from 'app/shared/services/device-detector.service';
import { CommonModule } from '@angular/common';
import { BrowserNotSupportedComponent } from '../browser-not-supported/browser-not-supported.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [HeaderComponent,FooterComponent,RouterModule,SpinnerComponent,HttpClientModule,CommonModule,BrowserNotSupportedComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  
  isLoading:boolean =false;
  isMobile:boolean = false;

  constructor(private deviceDetector : DeviceDetectorService,private localService: LocalStorageService,private router:Router,private authService: AuthService,private navigationService : NavigationTrackerService) {}

  ngOnInit(): void {
    this.isMobile=this.deviceDetector.isMobileDevice();
    this.isLoading = true;
    let previousURL = this.navigationService.getPreviousUrl();
    if(this.isMobile) {
      this.isLoading = false;
    } else {
      if(previousURL=='/login') {
        this.router.navigate(['/home']);
        this.isLoading = false;
      } else {
        if(this.authService.getToken()!=null) {
          this.authService.ping().then(()=>{
            this.router.navigate(['/home'])
          }).catch(err=>{
            this.router.navigate(['/login']);
          }).finally(()=>{
            this.isLoading = false;
          });
        } else {
          this.isLoading = false;
          this.router.navigate(['/login']);
        }
      }
    }
  }
}
