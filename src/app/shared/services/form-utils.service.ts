import { Injectable } from '@angular/core';
import { MenuItem } from '../models/menu-item.model';
import { filter, Subscription } from 'rxjs';
import { NavigationEnd, Router } from '@angular/router';
import { appConfig } from '../constants/app-config.enum';
import { FormGroup, Validators } from '@angular/forms';
import { alphaNumericSpaceSpecialValidator, atLeastOneAlphabetValidator, minimumAmountValidator } from './app-validators';
@Injectable({
    providedIn: 'root'
})
export class FormUtilsService {
    
    buildTaskForm(context:any ): FormGroup<any> {
        return context.fb.group({
            taskID: [],
            enabled: ['active'],
            title: ['', [Validators.required, atLeastOneAlphabetValidator(),alphaNumericSpaceSpecialValidator(), Validators.maxLength(254)]],
            description: ['', [Validators.required, atLeastOneAlphabetValidator(),alphaNumericSpaceSpecialValidator(), Validators.maxLength(254)]],
            note: [''],
            displayURL: ['', [Validators.required]],
        });
    }
    
    buildFAQForm(context:any): FormGroup<any> {
        return context.fb.group({
            id:[],
            question: ['', [Validators.required, atLeastOneAlphabetValidator(),alphaNumericSpaceSpecialValidator(), Validators.maxLength(254)]],
            description: ['', [Validators.required, atLeastOneAlphabetValidator(),alphaNumericSpaceSpecialValidator(), Validators.maxLength(254)]],
        });
    }

    buildOptionForm(context:any): FormGroup<any> {
        return context.fb.group({
          id:[],
          name:['', [Validators.required, atLeastOneAlphabetValidator(),alphaNumericSpaceSpecialValidator(), Validators.maxLength(254)]],
          description:['', [Validators.required, atLeastOneAlphabetValidator(),alphaNumericSpaceSpecialValidator(), Validators.maxLength(254)]],
          serviceCategoryID:[],
          serviceTaskID:[],
          defaultAmount:[0,[Validators.required,minimumAmountValidator()]],
          taskDuration:[30,Validators.required]
        });
    }

    buildCategoryForm(context:any): FormGroup<any> {
        return context.fb.group({
            categoryId: [],
            enabled: ['active'],
            name: ['', [Validators.required,atLeastOneAlphabetValidator(),alphaNumericSpaceSpecialValidator(),Validators.maxLength(254)]],
            serviceProviders: [],
            availableLocations: [],
            displayURL: ['', [Validators.required,Validators.maxLength(254),atLeastOneAlphabetValidator(),alphaNumericSpaceSpecialValidator()]]
        });
    }
}