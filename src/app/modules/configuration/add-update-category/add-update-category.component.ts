import { Component, ElementRef, EventEmitter, inject, Inject, Input, OnDestroy, OnInit, Output, QueryList, ViewChildren } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ComponentState, CrumbItems, STATUS } from '../../../shared/constants/constants.enum';
import { BreadCrumbItem } from '../../../shared/models/bread-crumb-item.model';
import { AppUtilsService } from '../../../shared/services/app-utils.service';
import { CategoryService } from '../../../shared/services/categories.service';
import { finalize, forkJoin, Subscription } from 'rxjs';
import { SnackbarService } from '../../../shared/services/snackbar.service';
import { FormUtilsService } from 'app/shared/services/form-utils.service';
import { CategoryRequest } from 'app/shared/models/category';


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

  private formUtils:FormUtilsService = inject(FormUtilsService);
  private fb:FormBuilder = inject(FormBuilder);
  private appUtils: AppUtilsService = inject(AppUtilsService);
  private categoryService: CategoryService = inject(CategoryService);
  private snackbar: SnackbarService = inject(SnackbarService);

  private getCategorySubscription?: Subscription;
  private updateCategorySubscription?: Subscription;
  private deleteCategoryTaskSubscription?: Subscription;
  tabIndex: number = 0;

  addCrumbItems: BreadCrumbItem[] = CrumbItems.AddCategoryCrumbItems;
  updateCrumbItems: BreadCrumbItem[] = CrumbItems.UpdateCategoryCrumbItems;

  @Output() updateComponentEvent = new EventEmitter<any>();

  taskState = ComponentState.UPDATE;


  constructor() { }

  ngOnInit(): void {
    this.state = this.categoryID ? ComponentState.UPDATE : ComponentState.ADD;
    this.initCategoryForm();
    if (this.state == this.updateState) {
      this.isLoading = true;
      this.fetchCategoryDetails();
    }
  }

  initCategoryForm() {
    this.categoryForm = this.formUtils.buildCategoryForm(this);
  }

  fetchCategoryDetails() {
    this.isLoading = true;
    this.getCategorySubscription = forkJoin([
      this.categoryService.getCategory(this.categoryID),
    ]).pipe(
      finalize(() => {
        this.isLoading = false;
      })
    ).subscribe(([categoriesResponse]) => {
      if (categoriesResponse?.statusMsg[0]?.serviceCategoryTasks??false) {
        this.tasks = categoriesResponse?.statusMsg[0]?.serviceCategoryTasks || [];
        this.allTasks = categoriesResponse?.statusMsg[0]?.serviceCategoryTasks || [];
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
      let payload: CategoryRequest = { id: this.categoryID, name: this.categoryForm.get('name').value, displayURL: this.categoryForm.get('displayURL').value, enabled: this.categoryForm.get('enabled').value == 'active' };
      this.updateCategorySubscription = this.categoryService.saveCategory(payload).subscribe({
        next: (res) => {
          this.snackbar.show('Succesfully Updated Category Details!', STATUS.SUCCESS);
          this.isLoading = false;
        },
        error: (err) => {
          this.snackbar.show('Unable To Update Category Details!', STATUS.ERROR);
          this.isLoading = false;
        },
      });
    } else {
      let payload: CategoryRequest = { name: this.categoryForm.get('name').value, displayURL: this.categoryForm.get('displayURL').value, enabled: this.categoryForm.get('enabled').value == 'active' };
      this.updateCategorySubscription = this.categoryService.saveCategory(payload).subscribe({
        next: (res) => {
          this.categoryForm.reset();
          this.previewUrl = '';
          this.snackbar.show('Succesfully Saved Category Details!', STATUS.SUCCESS);
          this.isLoading = false;
        },
        error: (err) => {
          this.snackbar.show((err?.message??'Unable To Save Category Details!'), STATUS.ERROR);
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

  addNewCategory(event:any): void {
    event.preventDefault();
    this.state = this.addState;
    this.categoryID = null;
    this.taskID = null;
    this.previewUrl = null;
    this.tasks = [];
    this.initCategoryForm();
    this.categoryForm.markAsPristine();
    this.categoryForm.markAsUntouched();
    this.categoryForm.updateValueAndValidity();
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

  handleConfirmDeleteTaskAction() {
    this.isLoading = true;
    this.deleteCategoryTaskSubscription =  this.categoryService.deleteCategoryTask(this.categoryID,this.taskID).subscribe((res)=>{
      this.showDeleteModal = false;
      this.taskID = null;
      this.isLoading = false;
      this.snackbar.show('Deleted Task Succsefully',STATUS.SUCCESS);
      this.fetchCategoryDetails();
    },(err)=>{
      this.showDeleteModal = false;
      this.taskID = null;
      this.isLoading = false;
      this.snackbar.show('Unable To Delete Task',STATUS.ERROR);
    });
  }

  cancelConfirmDeleteTaskAction() {
    this.taskID = null;
    this.showDeleteModal = false;
  }
}