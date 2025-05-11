import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { SessionExtendDialogComponent } from '../components/session-extend-dialog/session-extend-dialog.component';

@Injectable({
  providedIn: 'root'
})
export class TokenExpiryService {
  private timeoutId: any;

  constructor(private dialog: MatDialog) {}

  scheduleExpiryPopup(expiresAt: string) {
    this.clearExpiryPopup();

    const expireTime = new Date(expiresAt).getTime();
    const now = Date.now();
    const timeUntilPopup = expireTime - now - 60000; // 1 minute before expiry

    if (timeUntilPopup > 0) {
      this.timeoutId = setTimeout(() => {
        this.showExpiryPopup();
      }, timeUntilPopup);
    }
  }

  clearExpiryPopup() {
    if (this.timeoutId) {
      clearTimeout(this.timeoutId);
      this.timeoutId = null;
    }
  }

  private showExpiryPopup() {
    const existingDialog = document.querySelector('app-session-extend-dialog');
    if (!existingDialog) {
      this.dialog.open(SessionExtendDialogComponent, {
        width: '400px',
        disableClose: true
      });
    }
  }
}
