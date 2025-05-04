import { Component, ElementRef, inject, Inject, Input, OnDestroy, OnInit, QueryList, ViewChildren } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ComponentState, CrumbItems } from 'app/shared/constants/constants.enum';
import RouteUrl from 'app/shared/constants/router-url.enum';
import { BreadCrumbItem } from 'app/shared/models/bread-crumb-item.model';
import { Validators } from '@angular/forms';
import { AppUtilsService } from 'app/shared/services/app-utils.service';
import { CategoryService } from 'app/shared/services/categories.service';
import { Subscription } from 'rxjs';


@Component({
  selector: 'app-add-update-category',
  standalone: false,
  templateUrl: './add-update-category.component.html',
  styleUrl: './add-update-category.component.scss'
})
export class AddUpdateCategoryComponent implements OnInit,OnDestroy {
  @Input()
  categoryID: string;
  
  state:string = ComponentState.ADD;
  addState = ComponentState.ADD;
  updateState = ComponentState.UPDATE;
  
  categoryForm: FormGroup;
  tasks = [];

  previewUrl = '';
  showAddTask:boolean =false;
  isLoading:boolean =false;

  @ViewChildren('formField') formFields!: QueryList<ElementRef>;

  private appUtils = inject(AppUtilsService);

  private getCategorySubscription? : Subscription;

  addCrumbItems:BreadCrumbItem[] = CrumbItems.AddCategoryCrumbItems;
  updateCrumbItems:BreadCrumbItem[] = CrumbItems.UpdateCategoryCrumbItems;
  
  constructor(private fb: FormBuilder,private categoryService:CategoryService) {}
  
  ngOnDestroy(): void {
    if(this.getCategorySubscription) {
      this.getCategorySubscription.unsubscribe();
    }
  }

  ngOnInit(): void {
    this.state = this.categoryID ? ComponentState.UPDATE : ComponentState.ADD;
    // if(this.state == this.updateState) {
    //   this.isLoading = true;
    //   this.fetchCategoryDetails();
    // }
    this.categoryForm = this.fb.group({
      categoryId: [],
      enabled: [true],
      name: ['',[Validators.required]],
      serviceProviders: [],
      availableLocations: [],
      displayUrl:['']
    });
  }

  fetchCategoryDetails() {
    this.getCategorySubscription = this.categoryService.getCategory(this.categoryID).subscribe({
      next : (res)=>{
        this.isLoading = false;
        if(res?.statusMsg?.length > 0) {
          let categoryData = res.statusMsg[0];
          this.categoryForm.get('name').setValue(categoryData.name);
          this.categoryForm.get('displayUrl').setValue(categoryData.displayURL);
          if(categoryData.displayURL) {
              this.previewUrl = categoryData.displayURL;
          }
          this.categoryForm.get('name').setValue(categoryData.name);
          this.categoryForm.get('categoryId').setValue(categoryData.id);
          this.categoryForm.get('enabled').setValue(categoryData.enabled);
        }
      },
      error:(error)=>{
        this.isLoading = false;
      },
    })
  }

  getTasksFromApi(term: string) {
    const allTasks = [
      { 'title': 'Setup project', 'body': 'Initial environment setup' },
      { 'title': 'Review docs', 'body': 'Go through the documentation' }
    ];
    this.tasks = allTasks.filter(t => t.title.toLowerCase().includes(term.toLowerCase()));
  }
  
  onSubmit() {
    if (this.categoryForm.invalid) {
      this.appUtils.focusFirstInvalidControl(this,this.categoryForm);
      this.categoryForm.markAllAsTouched();
      return;
    }
    console.log(this.categoryForm.get('name').value);
    console.log(this.categoryForm.get('description').value);
    console.log(this.categoryForm.get('displayUrl').value);
    console.log(this.categoryForm.get('status').value);
  }

  

  showTaskModal() {
    this.showAddTask = true;
  }

  cancelTask() {
    this.showAddTask = false;
  }

  saveTask(event) {

  }

  onUrlBlur(): void {
    this.previewUrl = this.categoryForm.get('displayUrl').value?.trim() || null;
  }
  
}