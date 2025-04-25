import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Category } from 'app/shared/models/category';
import { CategoryService } from 'app/shared/services/categories.service';

@Component({
  selector: 'app-list-category',
  standalone: false,
  templateUrl: './list-category.component.html',
  styleUrl: './list-category.component.scss'
})
export class ListCategoryComponent {
  categories: Category[] = [];
  apiLoading:boolean = false;
  
  constructor(private categoryService: CategoryService, private router: Router) {}

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories() {
    this.apiLoading = true;
    this.categoryService.listCategories().then(res=>{
      this.categories =  res?.statusMsg;
      this.categories.push({'id':1,"name":"Cleaning","providers":0,"locations":0});
    }).catch((err)=>{

    }).finally(()=>{
      this.apiLoading = false;
    });
  }

  editCategory(id: number) {
    console.log('Edit category with ID:', id);
  }

  addNewCategory() {
    console.log('Add new category');
  }

  goToDashboard() {
    this.router.navigate(['/dashboard']);
  }
}
