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

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [HeaderComponent,FooterComponent,RouterModule,SpinnerComponent,HttpClientModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  
  loading:boolean = false;

  constructor(private localService: LocalStorageService,private router:Router,private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.fetchRoles().then(()=>{
      this.router.navigate(['/home'])
    }).catch(err=>{
      this.router.navigate(['/login']);
    });
  }
}
