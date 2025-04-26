import { HttpClient } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Category } from 'app/shared/models/category';
import { AppUtilsService } from 'app/shared/services/app-utils.service';
import { CategoryService } from 'app/shared/services/categories.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-list-category',
  standalone: false,
  templateUrl: './list-category.component.html',
  styleUrl: './list-category.component.scss'
})
export class ListCategoryComponent implements OnInit,OnDestroy {
  categories: Category[] = [];
  apiLoading:boolean = false;
  
  private listCategoriesSubscription? : Subscription;

  headers = [
    { field: 'id', label: 'ID' },
    { field: 'name', label: 'Name' },
    { field: 'providers', label: 'Providers' },
    { field: 'locations', label: 'Locations' }
  ];

  pageSize = 5;

  constructor(private categoryService: CategoryService, private router: Router,private utils:AppUtilsService) {}
  
  ngOnDestroy(): void {
    this.utils.unsubscribeData([this.listCategoriesSubscription]);
  }

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories() {
    this.categories = [
          { id: 1, name: 'Home Cleaning', providers: 45, locations: 12 },
          { id: 2, name: 'Pest Control', providers: 30, locations: 8 },
          { id: 3, name: 'Plumbing', providers: 20, locations: 6 },
          { id: 4, name: 'Electricians', providers: 10, locations: 5 },
          { id: 5, name: 'Painting', providers: 15, locations: 7 },
          { id: 6, name: 'Carpentry', providers: 18, locations: 4 }
    ];
    // this.apiLoading = true;
    // this.listCategoriesSubscription = this.categoryService.listCategories().subscribe(res=>{
    //   this.categories =  res?.statusMsg;
    //   this.categories = [
    //     { id: 1, name: 'Home Cleaning', providers: 45, locations: 12 },
    //     { id: 2, name: 'Pest Control', providers: 30, locations: 8 },
    //     { id: 3, name: 'Plumbing', providers: 20, locations: 6 },
    //     { id: 4, name: 'Electricians', providers: 10, locations: 5 },
    //     { id: 5, name: 'Painting', providers: 15, locations: 7 },
    //     { id: 6, name: 'Carpentry', providers: 18, locations: 4 }
    //   ];
    //   console.log(this.categories);
    //   this.apiLoading = false;
    // },((err)=>{
    //   this.apiLoading = false;
    // }));
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
