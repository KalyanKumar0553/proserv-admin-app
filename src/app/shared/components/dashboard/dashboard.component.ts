import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { AppRoutingModule } from 'app/app.routing';
import { Router, RouterModule } from '@angular/router';
import { SidenavComponent } from '../sidenav/sidenav.component';
import { SpinnerComponent } from '../spinner/spinner.component';
import { LocalStorageService } from 'app/shared/services/local-service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [HeaderComponent,FooterComponent,RouterModule,SidenavComponent,SpinnerComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  
  loading:boolean = false;

  constructor(private localService: LocalStorageService,private router:Router) {}

  ngOnInit(): void {
    if(this.localService.getData('token')) {
      this.router.navigateByUrl('/home');
    } else {
      this.router.navigateByUrl('/login');
    }
  }
}
