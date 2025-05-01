import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-confirmation-modal',
  imports: [CommonModule],
  templateUrl: './confirmation-modal.component.html',
  styleUrl: './confirmation-modal.component.scss'
})
export class ConfirmationModalComponent {
  @Input() title: string = 'Confirm';
  @Input() message: string = 'Are you sure?';
  @Input() showModal: boolean = false;
  @Input() requestInProgress: boolean = false;

  @Output() confirmAction = new EventEmitter<void>();
  @Output() cancelAction = new EventEmitter<void>();

  onConfirm(): void {
    this.confirmAction.emit();
  }

  onCancel(): void {
    this.cancelAction.emit();
  }
}
