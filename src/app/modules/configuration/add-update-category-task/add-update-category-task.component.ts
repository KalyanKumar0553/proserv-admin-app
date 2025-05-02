import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-add-update-category-task',
  standalone: false,
  templateUrl: './add-update-category-task.component.html',
  styleUrl: './add-update-category-task.component.scss'
})
export class AddUpdateCategoryTaskComponent implements OnInit {
  
  @Input() showModal: boolean = false;
  @Input() requestInProgress: boolean = false;

  @Output() saveAction = new EventEmitter<void>();
  @Output() cancelAction = new EventEmitter<void>();

  ngOnInit(): void {
      console.log(this.showModal);
  }

  onSave(): void {
    this.saveAction.emit();
  }

  onCancel(): void {
    this.cancelAction.emit();
  }
}
