import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { COMMA, ENTER } from '@angular/cdk/keycodes';
import { MatChipEditedEvent, MatChipInputEvent } from '@angular/material/chips';
import { ComponentState } from 'app/shared/constants/constants.enum';

@Component({
  selector: 'app-add-update-category-task',
  standalone: false,
  templateUrl: './add-update-category-task.component.html',
  styleUrl: './add-update-category-task.component.scss'
})
export class AddUpdateCategoryTaskComponent implements OnInit {

  @Input() showModal: boolean = false;
  @Input() requestInProgress: boolean = false;
  @Input() state: string = ComponentState.UPDATE;

  @Output() saveAction = new EventEmitter<void>();
  @Output() cancelAction = new EventEmitter<void>();

  faqState:string = 'list-faq';

  taskForm: FormGroup;
  addFaqForm: FormGroup;

  faqs = [];
  separatorKeysCodes: number[] = [ENTER, COMMA];
  inclusions: string[] = [];
  exclusions: string[] = [];
  taskOptions: any = [];

  addState = ComponentState.ADD;
  updateState = ComponentState.UPDATE;

  constructor(private fb: FormBuilder) { }

  ngOnInit(): void {
    this.taskForm = this.fb.group({
      taskId: ['#CAT-001'],
      status: ['Active'],
      name: ['Home Cleaning'],
      description: ['Professional home cleaning services including regular cleaning, deep cleaning, and specialized services for various areas of your home.'],
      notes: ['Notes To be included for the Task'],
      displayURL: ""
    });
    this.addFaqForm = this.fb.group({
      question: [''],
      description: [''],
    });
    this.loadServiceOptions();
  }

  onSave(): void {
    this.saveAction.emit();
  }

  onCancel(): void {
    this.cancelAction.emit();
  }


  getFaqsFromApi(term: string) {
    const allFaqs = this.faqs; // fetch search faq based on term from api
    this.faqs = allFaqs.filter(f => f.title.toLowerCase().includes(term.toLowerCase()));
  }

  onEditFAQ(item: any) {

  }

  onDeleteFAQ(item: any) {

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
    this.faqs.push({'title':this.addFaqForm.get('question').value,'body':this.addFaqForm.get('description').value});
    this.addFaqForm.reset();
    this.faqState = 'list-faq';
  }

  loadServiceOptions() {
    this.taskOptions = [
      {name:'2 BHK Cleaning',descripption:'',defaultAmount:110,displayURL:''},
      {name:'Full house cleaning',descripption:'',defaultAmount:200,displayURL:''},
    ];
  }

  editOption(option: any) {
    console.log('Edit', option);
  }

  deleteOption(option: any) {
    console.log('Delete', option);
  }
}
