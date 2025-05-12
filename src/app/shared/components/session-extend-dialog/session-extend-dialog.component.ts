import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import RouteUrl from '../../../shared/constants/router-url.enum';
import { AuthService } from '../../services/auth.service';
import { LocalStorageService } from '../../services/local-service';
import { TokenExpiryService } from '../../services/token-expiry.service';
import { interval, Subscription } from 'rxjs';

@Component({
  selector: 'app-session-extend-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule
  ],
  templateUrl: './session-extend-dialog.component.html',
  styleUrl: './session-extend-dialog.component.scss'
})
export class SessionExtendDialogComponent implements OnInit, OnDestroy{

  countdownSeconds = 120; // 2 minutes
  countdownDisplay = '02:00';
  private timerSub?: Subscription;
  isLoading = false;

  constructor(
    private dialogRef: MatDialogRef<SessionExtendDialogComponent>,
    private authService: AuthService,
    private tokenExpireService: TokenExpiryService,
    private localService: LocalStorageService,
    private router: Router
  ) {}


  ngOnInit(): void {
    this.startCountdown();
  }

  private startCountdown(): void {
    this.timerSub = interval(1000).subscribe(() => {
      this.countdownSeconds--;
      const minutes = Math.floor(this.countdownSeconds / 60);
      const seconds = this.countdownSeconds % 60;
      this.countdownDisplay = `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
      if (this.countdownSeconds <= 0) {
        this.logout(); // or auto-close: this.dialogRef.close();
      }
    });
  }

  extend(): void {
    this.isLoading = true;
    this.authService.refreshToken().subscribe({
      next: (res) => {
        this.authService.setUserSession(res?.statusMsg?.accessToken);
        this.authService.updateExpiration(res?.statusMsg?.expiresAt);
        this.tokenExpireService.scheduleExpiryPopup(res?.statusMsg?.expiresAt);
        this.isLoading = false;
        this.dialogRef.close();
      },
      error: () => {
        this.isLoading = false;
        this.dialogRef.close();
      }
    });
  }

  logout(): void {
    this.isLoading = true;
    this.authService.logoutUser().toPromise().then(()=>{
      this.localService.clearData();
      this.authService.clearUserData();
      this.isLoading = false;
      this.dialogRef.close();
      this.router.navigateByUrl(RouteUrl.LOGIN);
    }).catch(()=>{
      this.isLoading = false;
      this.dialogRef.close();
    });
  }

  ngOnDestroy(): void {
    this.timerSub?.unsubscribe();
  }
}
