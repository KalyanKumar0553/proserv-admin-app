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
      {'label' : 'Add / Update Category','isTail':true},
  ];

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.state = this.categoryID ? "UPDATE" : "ADD";
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
    const allTasks = [
      { 'title': 'Setup project', 'body': 'Initial environment setup' },
      { 'title': 'Review docs', 'body': 'Go through the documentation' }
    ];
    this.tasks = allTasks.filter(t => t.title.toLowerCase().includes(term.toLowerCase()));
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