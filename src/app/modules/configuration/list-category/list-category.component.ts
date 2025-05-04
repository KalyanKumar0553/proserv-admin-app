import { HttpClient } from '@angular/common/http';
import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import RouteUrl from 'app/shared/constants/router-url.enum';
import { BreadCrumbItem } from 'app/shared/models/bread-crumb-item.model';
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
  showDeleteModal = false;

  private listCategoriesSubscription? : Subscription;


  @Output() updateComponentEvent = new EventEmitter<any>();


  headers = [
    { field: 'id', label: 'ID' },
    { field: 'name', label: 'Name' },
    { field: 'providers', label: 'Providers' },
    { field: 'locations', label: 'Locations' },
    { field: 'enabled', label: 'Enabled' }
  ];

  crumbItems:BreadCrumbItem[] = [
    {'label' : 'Home' , 'route' : RouteUrl.HOME, 'component':''},
    {'label' : 'Categories'},
    {'label' : 'View Categories','isTail':true},
  ];

  delteItem: any = {};
  pageSize = 5;

  constructor(private categoryService: CategoryService, private router: Router,private utils:AppUtilsService) {}
  
  ngOnDestroy(): void {
    this.utils.unsubscribeData([this.listCategoriesSubscription]);
  }

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories() {
    this.apiLoading = true;
    this.listCategoriesSubscription = this.categoryService.listCategories().subscribe(res=>{
      this.categories =  res?.statusMsg;
      this.apiLoading = false;
    },((err)=>{
      this.apiLoading = false;
    }));
  }

  editCategory(item: any) {
    this.updateComponentEvent.emit({
      component:'add-update-category',
      state:{id:item.id}
    });
  }

  deleteCategory(item: any) {
    this.delteItem = item;
    this.showDeleteModal = true;
  }

  addNewCategory() {
    this.updateComponentEvent.emit({
      component:'add-update-category',
      state:{}
    });
  }

  goToDashboard() {
    this.router.navigate(['/dashboard']);
  }

  handleConfirmDeleteAction() {
    this.delteItem = null;
    this.showDeleteModal = false;
  }

  cancelConfirmAction() {
    this.delteItem = null;
    this.showDeleteModal = false;
  }

  getConfirmationTitle() {
    return `Are you sure you want to delete ${this.delteItem.name} ?`
  }
}
