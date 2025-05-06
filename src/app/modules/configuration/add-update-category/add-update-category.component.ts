import { Component, ElementRef, EventEmitter, inject, Inject, Input, OnDestroy, OnInit, Output, QueryList, ViewChildren } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ComponentState, CrumbItems, STATUS } from 'app/shared/constants/constants.enum';
import RouteUrl from 'app/shared/constants/router-url.enum';
import { BreadCrumbItem } from 'app/shared/models/bread-crumb-item.model';
import { Validators } from '@angular/forms';
import { AppUtilsService } from 'app/shared/services/app-utils.service';
import { CategoryService } from 'app/shared/services/categories.service';
import { finalize, forkJoin, Subscription } from 'rxjs';
import { SnackbarService } from 'app/shared/services/snackbar.service';
import { CreateCategoryRequest, UpdateCategoryRequest } from 'app/shared/models/category';


@Component({
  selector: 'app-add-update-category',
  standalone: false,
  templateUrl: './add-update-category.component.html',
  styleUrl: './add-update-category.component.scss'
})
export class AddUpdateCategoryComponent implements OnInit, OnDestroy {
  @Input()
  categoryID: string;

  state: string = ComponentState.ADD;
  addState = ComponentState.ADD;
  updateState = ComponentState.UPDATE;

  categoryForm: FormGroup;
  tasks = [];
  allTasks = [];
  taskID:number=0;
  previewUrl = '';
  showAddTask: boolean = false;
  isLoading: boolean = false;
  showDeleteModal: boolean = false;
  @ViewChildren('formField') formFields!: QueryList<ElementRef>;

  private appUtils = inject(AppUtilsService);
  
  apiLoading:boolean =false;

  private getCategorySubscription?: Subscription;
  private updateCategorySubscription?: Subscription;
  private deleteCategoryTaskSubscription?: Subscription;

  addCrumbItems: BreadCrumbItem[] = CrumbItems.AddCategoryCrumbItems;
  updateCrumbItems: BreadCrumbItem[] = CrumbItems.UpdateCategoryCrumbItems;

  @Output() updateComponentEvent = new EventEmitter<any>();

  taskState = ComponentState.UPDATE;


  constructor(private fb: FormBuilder, private categoryService: CategoryService, private snackservice: SnackbarService) { }

  ngOnInit(): void {
    this.state = this.categoryID ? ComponentState.UPDATE : ComponentState.ADD;
    if (this.state == this.updateState) {
      this.isLoading = true;
      this.fetchCategoryDetails();
    }
    this.categoryForm = this.fb.group({
      categoryId: [],
      enabled: ['active'],
      name: ['', [Validators.required]],
      serviceProviders: [],
      availableLocations: [],
      displayURL: ['', [Validators.required]]
    });
  }

  fetchCategoryDetails() {
    this.getCategorySubscription = forkJoin([
      this.categoryService.getCategory(this.categoryID),
      this.categoryService.getCategoryTasks(this.categoryID),
    ]).pipe(
      finalize(() => {
        this.isLoading = false;
      })
    ).subscribe(([categoriesResponse, tasksResponse]) => {
      if (tasksResponse?.statusMsg?.length > 0) {
        this.tasks = tasksResponse.statusMsg;
        this.allTasks = tasksResponse.statusMsg;
        console.log(tasksResponse);
      }
      if (categoriesResponse?.statusMsg?.length > 0) {
        let categoryData = categoriesResponse.statusMsg[0];
        this.categoryForm.get('name').setValue(categoryData.name);
        this.categoryForm.get('displayURL').setValue(categoryData.displayURL);
        if (categoryData.displayURL) {
          this.previewUrl = categoryData.displayURL;
        }
        this.categoryForm.get('name').setValue(categoryData.name);
        this.categoryForm.get('categoryId').setValue(categoryData.id);
        this.categoryForm.get('serviceProviders').setValue(0);
        this.categoryForm.get('availableLocations').setValue(0);
        this.categoryForm.get('enabled').setValue(categoryData.enabled ? 'active' : 'inactive');
      }
    });
  }

  getTasksFromApi(term: string) {
    this.tasks = this.allTasks.filter(t => t.title.toLowerCase().includes(term.toLowerCase()));
  }

  onSubmit() {
    if (this.categoryForm.invalid) {
      this.appUtils.focusFirstInvalidControl(this, this.categoryForm);
      this.categoryForm.markAllAsTouched();
      return;
    }
    this.isLoading = true;
    if (this.state == this.updateState) {
      let payload: UpdateCategoryRequest = { id: this.categoryID, name: this.categoryForm.get('name').value, displayURL: this.categoryForm.get('displayURL').value, enabled: this.categoryForm.get('enabled').value == 'active' };
      this.updateCategorySubscription = this.categoryService.updateCategory(this.categoryID, payload).subscribe({
        next: (res) => {
          this.snackservice.show('Succesfully Updated Category Details!', STATUS.SUCCESS);
          this.isLoading = false;
        },
        error: (err) => {
          this.snackservice.show('Unable To Update Category Details!', STATUS.ERROR);
          this.isLoading = false;
        },
      });
    } else {
      let payload: CreateCategoryRequest = { name: this.categoryForm.get('name').value, displayURL: this.categoryForm.get('displayURL').value, enabled: this.categoryForm.get('enabled').value == 'active' };
      this.updateCategorySubscription = this.categoryService.saveCategory(payload).subscribe({
        next: (res) => {
          this.categoryForm.reset();
          this.previewUrl = '';
          this.snackservice.show('Succesfully Saved Category Details!', STATUS.SUCCESS);
          this.isLoading = false;
        },
        error: (err) => {
          this.snackservice.show('Unable To Save Category Details!', STATUS.ERROR);
          this.isLoading = false;
        }
      });
    }
  }



  showTaskModal(state: any) {
    this.showAddTask = true;
    this.taskState = state;
  }

  onEditTask(state:any) {
    this.taskState = this.updateState;
    this.taskID = state.id;
    this.showAddTask = true;
  }

  onDeleteTask(state:any) {
      this.taskID = state.id;
      this.showDeleteModal = true;
  }

  cancelTask() {
    this.showAddTask = false;
    this.taskID = 0;
  }

  saveTask() {
    this.showAddTask = false;
    this.fetchCategoryDetails();
  }

  onUrlBlur(): void {
    this.previewUrl = this.categoryForm.get('displayURL').value?.trim() || null;
  }


  ngOnDestroy(): void {
    this.categoryID = null;
    this.taskID = null;
    if (this.getCategorySubscription) {
      this.getCategorySubscription.unsubscribe();
    }
    if (this.updateCategorySubscription) {
      this.updateCategorySubscription.unsubscribe();
    }
    if(this.deleteCategoryTaskSubscription) {
      this.deleteCategoryTaskSubscription.unsubscribe();
    }
    this.categoryForm.reset();
  }

  handleConfirmDeleteAction() {
    this.apiLoading = true;
    this.deleteCategoryTaskSubscription =  this.categoryService.deleteCategoryTask(this.categoryID,this.taskID).subscribe((res)=>{
      this.showDeleteModal = false;
      this.taskID = null;
      this.apiLoading = false;
      this.snackservice.show('Delete Task Succsefully',STATUS.SUCCESS);
    },(err)=>{
      this.showDeleteModal = false;
      this.taskID = null;
      this.apiLoading = false;
      this.snackservice.show('Unable To Delete Task',STATUS.ERROR);
    });
  }

  cancelConfirmAction() {
    this.taskID = null;
    this.showDeleteModal = false;
  }
}