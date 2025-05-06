import { NgClass, NgIf } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { Component, Inject } from '@angular/core';
import { MAT_SNACK_BAR_DATA, MatSnackBarRef } from '@angular/material/snack-bar';
import { STATUS } from 'app/shared/constants/constants.enum';


@Component({
  selector: 'app-snackbar',
  imports: [NgClass, MatIconModule],
  templateUrl: './snackbar.component.html',
  styleUrl: './snackbar.component.scss'
})
export class SnackbarComponent {
  constructor(@Inject(MAT_SNACK_BAR_DATA) public data: { message: string; type: string },private snackBarRef: MatSnackBarRef<SnackbarComponent>) {}

  getIcon(type: string): string {
    switch (type) {
      case STATUS.SUCCESS: return 'check_circle';
      case STATUS.ERROR: return 'error';
      case STATUS.INFO: return 'info';
      case STATUS.WARN: return 'warning';
      default: return 'info';
    }
  }

  close() {
    this.snackBarRef.dismiss();
  }
}
