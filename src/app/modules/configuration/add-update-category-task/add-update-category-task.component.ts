import { Component, ElementRef, EventEmitter, inject, Input, OnDestroy, OnInit, Output, QueryList, ViewChildren } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { COMMA, ENTER } from '@angular/cdk/keycodes';
import { MatChipEditedEvent, MatChipInputEvent } from '@angular/material/chips';
import { ComponentState, STATUS } from '../../../shared/constants/constants.enum';
import { AppUtilsService } from '../../../shared/services/app-utils.service';
import { CategoryTaskRequest, FAQRequest, TaskOptionRequest } from '../../../shared/models/category';
import { CategoryService } from '../../../shared/services/categories.service';
import { SnackbarService } from '../../../shared/services/snackbar.service';
import { finalize, forkJoin, Subscription } from 'rxjs';
import { FormUtilsService } from 'app/shared/services/form-utils.service';

@Component({
  selector: 'app-add-update-category-task',
  standalone: false,
  templateUrl: './add-update-category-task.component.html',
  styleUrl: './add-update-category-task.component.scss'
})
export class AddUpdateCategoryTaskComponent implements OnInit, OnDestroy {

  @Input() showModal: boolean = false;
  @Input() state: string = ComponentState.UPDATE;
  @Input() categoryID: number = 0;
  @Input() taskID: number = 0;
  @Output() saveAction = new EventEmitter<void>();
  @Output() cancelAction = new EventEmitter<void>();

  @ViewChildren('formField') formFields!: QueryList<ElementRef>;
  faqState: string = 'list-faq';
  optionState: string = 'list-option';
  
  addState = ComponentState.ADD;
  updateState = ComponentState.UPDATE;
  isLoading: boolean = false;

  previewUrl = '';

  taskForm: FormGroup;
  faqForm: FormGroup;
  optionForm: FormGroup;

  allFaqs: FAQRequest[] = [];
  filteredFaqs: FAQRequest[] = [];
  separatorKeysCodes: number[] = [ENTER, COMMA];
  
  inclusions: string[] = [];
  exclusions: string[] = [];
  taskOptions: any = [];

  taskOptionInclusions: string[][] = [];
  taskOptionExclusions: string[][] = [];

  optionInclusions:string[] = [];
  optionExclusions:string[] = [];

  private updateCategoryTaskSubscription?: Subscription;
  private updateTaskOptionSubscription ?: Subscription;
  private getCategoryTaskSubscription?: Subscription;
  private faqSubscripiton?: Subscription;

  private formUtils:FormUtilsService = inject(FormUtilsService);
  private fb:FormBuilder = inject(FormBuilder);
  private appUtils: AppUtilsService = inject(AppUtilsService);
  private categoryService: CategoryService = inject(CategoryService);
  private snackbar: SnackbarService = inject(SnackbarService);
  
  constructor() { }
  
  ngOnInit(): void {
    this.taskForm = this.formUtils.buildTaskForm(this);
    this.faqForm = this.formUtils.buildFAQForm(this);
    this.optionForm = this.formUtils.buildOptionForm(this);
    if (this.state == this.updateState) {
      this.isLoading = true;
      this.loadTaskDetails();
    }
  }


  loadTaskDetails() {
    this.isLoading = true;
    this.getCategoryTaskSubscription = forkJoin([
      this.categoryService.getCategoryTaskByID(this.categoryID, this.taskID),
    ]).pipe(
      finalize(() => {
        this.isLoading = false;
      })
    ).subscribe(([taskResponse]) => {
      // this.isLoading = false;
      if (taskResponse?.statusMsg[0]?.faqs) {
        this.allFaqs = taskResponse?.statusMsg[0]?.faqs || [];
        this.filteredFaqs = taskResponse?.statusMsg[0]?.faqs || [];
      }
      if (taskResponse?.statusMsg[0]?.options?.length ?? false) {
        this.taskOptionInclusions = [];
        this.taskOptionExclusions = [];
        this.taskOptions = taskResponse?.statusMsg[0]?.options || [];
        this.taskOptions.forEach(o => {
          this.taskOptionInclusions.push((o?.inclusions||'').split(","));
          this.taskOptionExclusions.push((o?.exclusions||'').split(","));
        });
      }
      if (taskResponse?.statusMsg?.length > 0) {
        let taskData = taskResponse.statusMsg[0];
        this.taskForm.get('title').setValue(taskData.title);
        this.taskForm.get('displayURL').setValue(taskData.displayURL);
        if (taskData.displayURL) {
          this.previewUrl = taskData.displayURL;
        }
        this.taskForm.get('taskID').setValue(taskData.taskID);
        this.taskForm.get('description').setValue(taskData.description);
        this.taskForm.get('note').setValue(taskData.note);
        this.inclusions = (taskData?.inclusions||'').split(",");
        this.exclusions = (taskData?.exclusions||'').split(",");
      }
    });
  }

  onSave(): void {
    if (this.taskForm.invalid) {
      this.appUtils.focusFirstInvalidControl(this, this.taskForm);
      this.taskForm.markAllAsTouched();
      return;
    }
    this.isLoading = true;
    if (this.state == this.updateState) {
      let payload: CategoryTaskRequest = {
        id: this.taskID,
        serviceCategoryID: this.categoryID,
        title: this.taskForm.get('title').value,
        description: this.taskForm.get('description').value,
        displayURL: this.taskForm.get('displayURL').value,
        inclusions: this.inclusions.toString(),
        exclusions: this.exclusions.toString(),
        note: this.taskForm.get('note').value,
        enabled: this.taskForm.get('enabled').value == 'active'
      };
      this.updateCategoryTaskSubscription = this.categoryService.saveCategoryTask(this.categoryID, payload).subscribe({
        next: (res) => {
          this.snackbar.show('Succesfully Updated Task Details!', STATUS.SUCCESS);
          this.isLoading = false;
          this.saveAction.emit();
        },
        error: (err) => {
          this.snackbar.show('Unable To Update Task Details!', STATUS.ERROR);
          this.isLoading = false;
        },
      });
    } else {
      let payload: CategoryTaskRequest = {
        serviceCategoryID: this.categoryID,
        title: this.taskForm.get('title').value,
        description: this.taskForm.get('description').value,
        displayURL: this.taskForm.get('displayURL').value,
        inclusions: this.inclusions.toString(),
        exclusions: this.exclusions.toString(),
        note: this.taskForm.get('note').value,
        enabled: this.taskForm.get('enabled').value == 'active'
      };
      this.updateCategoryTaskSubscription = this.categoryService.saveCategoryTask(this.categoryID, payload).subscribe({
        next: (res) => {
          this.taskForm.reset();
          this.previewUrl = '';
          this.snackbar.show('Succesfully Saved Task Details!', STATUS.SUCCESS);
          this.saveAction.emit();
          this.isLoading = false;
        },
        error: (err) => {
          this.snackbar.show('Unable To Save Task Details!', STATUS.ERROR);
          this.isLoading = false;
        }
      });
    }
  }

  onTabChange(event: any): void {
    const tabIndex = event.index;
    this.faqForm.reset();
    if(tabIndex!=0) {
      this.faqState = 'list-faq';
      this.optionState = 'list-option';
    }
  }

  onCancel(): void {
    this.cancelAction.emit();
  }

  onUrlBlur(): void {
    this.previewUrl = this.taskForm.get('displayURL').value?.trim() || null;
  }

  searchFAQ(term: string = '') {
      this.filteredFaqs = term ? this.allFaqs.filter(f => f.question.toLowerCase().includes(term.toLowerCase())) : this.allFaqs;
  }

  onEditFAQ(item: any) {
    this.faqForm.get('id').setValue(item.id);
    this.faqForm.get('question').setValue(item.question);
    this.faqForm.get('description').setValue(item.answer);
    this.faqState = 'add-faq';
  }

  onDeleteFAQ(item: any) {
    if(item.id) {
      this.isLoading = true;
      this.faqSubscripiton = this.categoryService.deleteFAQ(this.categoryID,this.taskID,item.id).subscribe((res)=>{
        this.isLoading = false;
        this.snackbar.show('Succesfully Deleted FAQ Details!', STATUS.SUCCESS);
        this.loadTaskDetails();
      },(err)=>{
        this.isLoading = false;
        this.snackbar.show('Unable to Delete FAQ Details!', STATUS.ERROR);
      });
    }
  }

  addInclusion(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();
    if (value) {
      this.inclusions.push(value);
    }
    event.chipInput!.clear();
  }

  removeInclusion(item: string): void {
    const index = this.inclusions.indexOf(item);
    if (index >= 0) {
      this.inclusions.splice(index, 1);
    }
  }

  addExclusion(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();
    if (value) {
      this.exclusions.push(value);
    }
    event.chipInput!.clear();
  }

  removeExclusion(item: string): void {
    const index = this.exclusions.indexOf(item);
    if (index >= 0) {
      this.exclusions.splice(index, 1);
    }
  }

  editInclusion(item: string, event: MatChipEditedEvent) {
    const value = event.value.trim();
    if (!value) {
      this.removeInclusion(item);
      return;
    }
    const index = this.inclusions.indexOf(item);
    if (index >= 0) {
      this.inclusions[index] = value;
    }
  }

  editExclusion(item: string, event: MatChipEditedEvent) {
    const value = event.value.trim();
    if (!value) {
      this.removeExclusion(item);
      return;
    }
    const index = this.exclusions.indexOf(item);
    if (index >= 0) {
      this.exclusions[index] = value;
    }
  }

  addOptionInclusion(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();
    if (value) {
      this.optionInclusions.push(value);
    }
    event.chipInput!.clear();
  }

  removeOptionInclusion(item: string): void {
    const index = this.optionInclusions.indexOf(item);
    if (index >= 0) {
      this.optionInclusions.splice(index, 1);
    }
  }

  addOptionExclusion(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();
    if (value) {
      this.optionExclusions.push(value);
    }
    event.chipInput!.clear();
  }

  removeOptionExclusion(item: string): void {
    const index = this.optionExclusions.indexOf(item);
    if (index >= 0) {
      this.optionExclusions.splice(index, 1);
    }
  }

  editOptionInclusion(item: string, event: MatChipEditedEvent) {
    const value = event.value.trim();
    if (!value) {
      this.removeOptionInclusion(item);
      return;
    }
    const index = this.optionInclusions.indexOf(item);
    if (index >= 0) {
      this.optionInclusions[index] = value;
    }
  }

  editOptionExclusion(item: string, event: MatChipEditedEvent) {
    const value = event.value.trim();
    if (!value) {
      this.removeOptionExclusion(item);
      return;
    }
    const index = this.optionExclusions.indexOf(item);
    if (index >= 0) {
      this.optionExclusions[index] = value;
    }
  }

  addFAQState() {
    this.faqState = 'add-faq';
  }

  addTaskOptionState() {
    this.optionState = 'add-option';
  }

  cancelAddFAQ() {
    this.faqState = 'list-faq';
    this.searchFAQ();
    this.faqForm.reset();
  }

  saveOption() {
    if (this.optionForm.invalid) { 
      this.appUtils.focusFirstInvalidControl(this, this.optionForm);
      this.optionForm.markAllAsTouched();
      return;
    }
    this.isLoading = true;
    let payload : TaskOptionRequest = {
      'id': this.optionState=='add-option' ? null : this.optionForm.get('id').value, 
      'name':this.optionForm.get('name').value, 
      'description':this.optionForm.get('description').value, 
      'defaultAmount':this.optionForm.get('defaultAmount').value,
      'taskDuration':this.optionForm.get('taskDuration').value,
      'inclusions':this.optionInclusions.toString(),
      'exclusions':this.optionExclusions.toString(),
      'serviceTaskID':this.taskID, 
      'serviceCategoryID':this.categoryID,
    };
    this.updateCategoryTaskSubscription = this.categoryService.saveTaskOption(this.categoryID,payload).subscribe({
      next: (res) => {
        this.snackbar.show('Succesfully Updated Option Details!', STATUS.SUCCESS);
        this.isLoading = false;
        this.saveAction.emit();
      },
      error: (err) => {
        this.snackbar.show('Unable To Update Option Details!', STATUS.ERROR);
        this.isLoading = false;
      },
    });
  }

  saveFAQ() {
    if (this.faqForm.invalid) {
      this.appUtils.focusFirstInvalidControl(this, this.faqForm);
      this.faqForm.markAllAsTouched();
      return;
    }
    this.isLoading = true;
    let payload = { 'id':this.faqForm.get('id').value, 'question': this.faqForm.get('question').value, 'answer': this.faqForm.get('description').value, 'serviceTaskID':this.taskID, 'serviceCategoryID':this.categoryID };
    this.faqSubscripiton = this.categoryService.saveFAQ(payload).subscribe(()=>{
      this.isLoading = false;
      this.faqForm.reset();
      this.faqState = 'list-faq';
      this.loadTaskDetails();
      this.snackbar.show('Succesfully Saved FAQ Details!', STATUS.SUCCESS);
    },(err)=>{
      this.isLoading = false;
      this.snackbar.show('Error while Saving FAQ !', STATUS.ERROR);
    });
  }

  loadServiceOptions() {
    this.taskOptions = [
      { name: '2 BHK Cleaning', descripption: '', defaultAmount: 110, displayURL: '' },
      { name: 'Full house cleaning', descripption: '', defaultAmount: 200, displayURL: '' },
    ];
  }

  editOption(option: any) {
    console.log('Edit', option);
  }

  deleteOption(option: any) {
    console.log('Delete', option);
  }

  ngOnDestroy(): void {
    this.categoryID = null;
    if (this.getCategoryTaskSubscription) {
      this.getCategoryTaskSubscription.unsubscribe();
    }
    if (this.updateCategoryTaskSubscription) {
      this.updateCategoryTaskSubscription.unsubscribe();
    }
    if(this.faqSubscripiton) {
      this.faqSubscripiton.unsubscribe();
    }
    if(this.taskForm) {
      this.taskForm.reset();
    }
  }

  cancelAddOption() {
    this.optionState = 'list-option';
    this.optionForm.reset();
  }
}
