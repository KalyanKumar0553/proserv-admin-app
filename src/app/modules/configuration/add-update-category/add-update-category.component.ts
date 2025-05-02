import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import RouteUrl from 'app/shared/constants/router-url.enum';
import { BreadCrumbItem } from 'app/shared/models/bread-crumb-item.model';

@Component({
  selector: 'app-add-update-category',
  standalone: false,
  templateUrl: './add-update-category.component.html',
  styleUrl: './add-update-category.component.scss'
})
export class AddUpdateCategoryComponent implements OnInit {
  @Input()
  categoryID: string;
  
  categoryName = '';
  state:string = "ADD";
  categoryForm: FormGroup;
  tasks = [];

  showAddTask:boolean =false;
  
  crumbItems:BreadCrumbItem[] = [
      {'label' : 'Home' , 'route' : RouteUrl.HOME, 'component':''},
      {'label' : 'Categories'},
      {'label' : 'Add Category','isTail':true},
  ];

  faqs = [
    {
      'title': 'What is included in the cleaning task?',
      'body': 'Each cleaning task includes dusting, wiping, and disinfecting surfaces based on the selected area.'
    },
    {
      'title': 'How long does a typical cleaning session take?',
      'body': 'It usually takes 1–2 hours depending on the number of tasks selected.'
    },
    {
      'title': 'Can I reschedule the service?',
      'body': 'Yes, services can be rescheduled at no extra cost up to 2 hours before the scheduled time.'
    },
    {
      'title': 'What is included in the cleaning task?',
      'body': 'Each cleaning task includes dusting, wiping, and disinfecting surfaces based on the selected area.'
    },
    {
      'title': 'How long does a typical cleaning session take?',
      'body': 'It usually takes 1–2 hours depending on the number of tasks selected.'
    },
    {
      'title': 'Can I reschedule the service?',
      'body': 'Yes, services can be rescheduled at no extra cost up to 2 hours before the scheduled time.'
    },
    {
      'title': 'What is included in the cleaning task?',
      'body': 'Each cleaning task includes dusting, wiping, and disinfecting surfaces based on the selected area.'
    },
    {
      'title': 'How long does a typical cleaning session take?',
      'body': 'It usually takes 1–2 hours depending on the number of tasks selected.'
    },
    {
      'title': 'Can I reschedule the service?',
      'body': 'Yes, services can be rescheduled at no extra cost up to 2 hours before the scheduled time.'
    },
    {
      'title': 'What is included in the cleaning task?',
      'body': 'Each cleaning task includes dusting, wiping, and disinfecting surfaces based on the selected area.'
    },
    {
      'title': 'How long does a typical cleaning session take?',
      'body': 'It usually takes 1–2 hours depending on the number of tasks selected.'
    },
    {
      'title': 'Can I reschedule the service?',
      'body': 'Yes, services can be rescheduled at no extra cost up to 2 hours before the scheduled time.'
    },
    {
      'title': 'Are cleaning supplies provided?',
      'body': 'Yes, our professionals come fully equipped with all necessary cleaning materials.'
    }
  ];

  constructor(private fb: FormBuilder) {

  }

  ngOnInit(): void {
    this.state = this.categoryID ? "EDIT" : "ADD";
    this.categoryForm = this.fb.group({
      categoryId: ['#CAT-001'],
      status: ['Active'],
      name: ['Home Cleaning'],
      description: ['Professional home cleaning services including regular cleaning, deep cleaning, and specialized services for various areas of your home.'],
      serviceProviders: [128],
      availableLocations: [42]
    });
  }

  getTasksFromApi(term: string) {
    // Simulated API
    const allTasks = [
      { 'title': 'Setup project', 'body': 'Initial environment setup' },
      { 'title': 'Review docs', 'body': 'Go through the documentation' }
    ];
    this.tasks = allTasks.filter(t => t.title.toLowerCase().includes(term.toLowerCase()));
  }
  
  getFaqsFromApi(term: string) {
    const allFaqs = [
      { 'title': 'Getting Started?', 'body': 'Use our quick start guide.' },
      { 'title': 'Requirements?', 'body': 'Check the compatibility guide.' }
    ];
    this.faqs = allFaqs.filter(f => f.title.toLowerCase().includes(term.toLowerCase()));
  }

  onEditFAQ(item:any) {

  }

  onDeleteFAQ(item:any) {

  }

  onSubmit() {

  }

  showTaskModal() {
    this.showAddTask = true;
  }

  cancelTask() {
    this.showAddTask = false;
  }

  saveTask(event) {

  }
}