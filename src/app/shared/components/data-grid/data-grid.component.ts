import { CommonModule } from '@angular/common';
import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-data-grid',
  imports: [CommonModule,FormsModule],
  templateUrl: './data-grid.component.html',
  styleUrl: './data-grid.component.scss'
})
export class DataGridComponent implements OnInit {

  @Input() headers: { field: string; label: string }[] = [];
  @Input() data: any[] = [];
  @Input() pageSize = 5;

  filters: { [key: string]: string } = {};
  showFilter: { [key: string]: boolean } = {};
  filteredData: any[] = [];
  sortField: string = '';
  sortDirection: 'asc' | 'desc' = 'asc';
  currentPage = 1;

  ngOnInit(): void {
    this.headers.forEach(header => {
      this.filters[header.field] = '';
      this.showFilter[header.field] = false;
    });
    this.applyFilters();
  }

  applyFilters(): void {
    this.filteredData = this.data.filter(entry =>
      this.headers.every(header =>
        this.filters[header.field] === '' ||
        (entry[header.field] && entry[header.field].toString().toLowerCase().includes(this.filters[header.field].toLowerCase()))
      )
    );
    this.applySort();
    this.currentPage = 1;
  }

  sortData(field: string): void {
    if (this.sortField === field) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortField = field;
      this.sortDirection = 'asc';
    }
    this.applySort();
  }

  applySort(): void {
    if (!this.sortField) return;
    this.filteredData.sort((a, b) => {
      const valueA = a[this.sortField];
      const valueB = b[this.sortField];
      if (valueA < valueB) return this.sortDirection === 'asc' ? -1 : 1;
      if (valueA > valueB) return this.sortDirection === 'asc' ? 1 : -1;
      return 0;
    });
  }

  toggleFilter(field: string): void {
    this.showFilter[field] = !this.showFilter[field];
    if (!this.showFilter[field]) {
      this.filters[field] = '';
      this.applyFilters();
    }
  }

  get paginatedData() {
    const start = (this.currentPage - 1) * this.pageSize;
    const end = start + this.pageSize;
    return this.filteredData.slice(start, end);
  }

  totalPages(): number {
    return Math.ceil(this.filteredData.length / this.pageSize);
  }
}