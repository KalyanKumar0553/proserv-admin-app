import { Injectable } from '@angular/core';
import { MenuItem } from '../models/menu-item.model';
import { filter, Subscription } from 'rxjs';
import { NavigationEnd, Router } from '@angular/router';
import { appConfig } from '../constants/app-config.enum';
import { FormGroup } from '@angular/forms';
@Injectable({
    providedIn: 'root'
})
export class ResponseProcessorService {

    constructor() { }

}