import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { debounceTime, Subject, Subscription } from 'rxjs';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
@Component({
  selector: 'app-data-accordion',
  imports: [CommonModule,FormsModule,BrowserModule,MatExpansionModule,MatInputModule,MatIconModule],
  templateUrl: './data-accordion.component.html',
  styleUrl: './data-accordion.component.scss'
})
export class DataAccordionComponent implements OnInit {
  @Input() id = '';
  @Input() title = '';
  @Input() items: any[] = [];
  @Input() titleKey = 'title';
  @Input() bodyKey = 'body';
  @Input() searchable: boolean = true;
  @Input() showEdit: boolean = false;
  @Input() showDelete: boolean = false;

  @Output() searchChanged = new EventEmitter<string>();
  @Output() editItem = new EventEmitter<any>();
  @Output() deleteItem = new EventEmitter<any>();

  searchTerm = '';
  private searchSubject = new Subject<string>();
  private subscription = new Subscription();

  ngOnInit() {
    console.log(this.items);
    this.subscription = this.searchSubject
      .pipe(debounceTime(300))
      .subscribe(term => this.searchChanged.emit(term));
  }

  onSearchChange() {
    this.searchSubject.next(this.searchTerm);
  }

  onEdit(item: any) {
    this.editItem.emit(item);
  }

  onDelete(item: any) {
    this.deleteItem.emit(item);
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}