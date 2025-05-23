import { Component, ElementRef, EventEmitter, Input, OnDestroy, OnInit, Output, QueryList, ViewChildren } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { COMMA, ENTER } from '@angular/cdk/keycodes';
import { MatChipEditedEvent, MatChipInputEvent } from '@angular/material/chips';
import { ComponentState, STATUS } from '../../../shared/constants/constants.enum';
import { AppUtilsService } from '../../../shared/services/app-utils.service';
import { CreateCategoryTaskRequest, UpdateCategoryTaskRequest } from '../../../shared/models/category';
import { CategoryService } from '../../../shared/services/categories.service';
import { SnackbarService } from '../../../shared/services/snackbar.service';
import { finalize, forkJoin, Subscription } from 'rxjs';
import { alphaNumericSpaceSpecialValidator, alphaNumericSpaceValidator, atLeastOneAlphabetValidator } from '../../../shared/services/app-validators';

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
  
  addState = ComponentState.ADD;
  updateState = ComponentState.UPDATE;
  isLoading: boolean = false;

  previewUrl = '';

  taskForm: FormGroup;
  addFaqForm: FormGroup;

  allFaqs = [];
  filteredFaqs = [];
  separatorKeysCodes: number[] = [ENTER, COMMA];
  
  inclusions: string[] = [];
  exclusions: string[] = [];
  taskOptions: any = [];

  
  private updateCategoryTaskSubscription?: Subscription;
  private getCategoryTaskSubscription?: Subscription;
  private faqSubscripiton?: Subscription;

  constructor(private fb: FormBuilder, private appUtils: AppUtilsService, private categoryService: CategoryService, private snackbar: SnackbarService) { }

  ngOnInit(): void {
    if (this.state == this.updateState) {
      this.isLoading = true;
      this.loadTaskDetails();
    }
    this.taskForm = this.fb.group({
      taskID: [],
      enabled: ['active'],
      title: ['', [Validators.required, atLeastOneAlphabetValidator(),alphaNumericSpaceSpecialValidator(), Validators.maxLength(254)]],
      description: ['', [Validators.required, atLeastOneAlphabetValidator(),alphaNumericSpaceSpecialValidator(), Validators.maxLength(254)]],
      note: [''],
      displayURL: ['', [Validators.required]],
    });
    this.addFaqForm = this.fb.group({
      id:[],
      question: ['', [Validators.required, atLeastOneAlphabetValidator(),alphaNumericSpaceSpecialValidator(), Validators.maxLength(254)]],
      description: ['', [Validators.required, atLeastOneAlphabetValidator(),alphaNumericSpaceSpecialValidator(), Validators.maxLength(254)]],
    });
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
      this.isLoading = false;
      if (taskResponse?.statusMsg[0]?.faqs) {
        this.allFaqs = taskResponse?.statusMsg[0]?.faqs || [];
        this.filteredFaqs = taskResponse?.statusMsg[0]?.faqs || [];
      }
      if (taskResponse?.statusMsg[0]?.options?.length ?? false) {
        this.taskOptions = taskResponse?.statusMsg[0]?.options || [];
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
      let payload: UpdateCategoryTaskRequest = {
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
      this.updateCategoryTaskSubscription = this.categoryService.updateCategoryTask(this.categoryID, this.taskID, payload).subscribe({
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
      let payload: CreateCategoryTaskRequest = {
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
    this.addFaqForm.reset();
    if(tabIndex!=0) {
      this.faqState = 'list-faq';
    }
  }

  onCancel(): void {
    this.cancelAction.emit();
  }

  onUrlBlur(): void {
    this.previewUrl = this.taskForm.get('displayURL').value?.trim() || null;
  }

  searchFAQ(term: string = '') {
    this.filteredFaqs = this.allFaqs.filter(f => f.title.toLowerCase().includes(term.toLowerCase()));
  }

  onEditFAQ(item: any) {
    this.addFaqForm.get('id').setValue(item.id);
    this.addFaqForm.get('question').setValue(item.question);
    this.addFaqForm.get('description').setValue(item.answer);
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

  addFAQState() {
    this.faqState = 'add-faq';
  }

  cancelAddFAQ() {
    this.faqState = 'list-faq';
    this.addFaqForm.reset();
  }

  saveFAQ() {
    if (this.addFaqForm.invalid) {
      this.appUtils.focusFirstInvalidControl(this, this.addFaqForm);
      this.addFaqForm.markAllAsTouched();
      return;
    }
    this.isLoading = true;
    let payload = { 'id':this.addFaqForm.get('id').value, 'question': this.addFaqForm.get('question').value, 'answer': this.addFaqForm.get('description').value, 'serviceTaskID':this.taskID, 'serviceCategoryID':this.categoryID };
    this.faqSubscripiton = this.categoryService.saveFAQ(payload).subscribe(()=>{
      this.isLoading = false;
      this.addFaqForm.reset();
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
    this.taskForm.reset();
  }
}
