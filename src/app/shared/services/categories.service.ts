import {Injectable} from '@angular/core';
import { Router } from '@angular/router';
import { LocalStorageService } from './local-service';
import { ApiUrls } from '../constants/constants.enum';
import { ProServApiService } from './proserv-api.service';
import { FAQRequest, CategoryRequest, CategoryTaskRequest, TaskOptionRequest } from '../models/category';
import RouteUrl from '../constants/router-url.enum';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  public clearDataTimer: any;

  constructor(private apiService:ProServApiService,private router: Router,private localService: LocalStorageService){ }

  listCategories() {
    return this.apiService.get(ApiUrls.CATEGORIES);
  }

  getCategory(id:any) {
    return this.apiService.get(ApiUrls.CATEGORIES+RouteUrl.SEPERATOR+id);
  }

  listCategoryTasks(categoryID:any) {
    return this.apiService.get(ApiUrls.CATEGORIES+RouteUrl.SEPERATOR+categoryID+ApiUrls.TASKS);
  }

  getCategoryTaskByID(categoryID:any,taskID:any) {
    return this.apiService.get(ApiUrls.CATEGORIES+RouteUrl.SEPERATOR+categoryID+ApiUrls.TASKS+RouteUrl.SEPERATOR+taskID);
  }

  getFAQByCategoryIDAndTaskID(categoryID:any,taskID:any) {
    return this.apiService.get(ApiUrls.CATEGORIES+RouteUrl.SEPERATOR+categoryID+ApiUrls.TASKS);
  }

  saveCategory(payload:CategoryRequest) {
    return payload.id ? this.apiService.update(ApiUrls.CATEGORIES+RouteUrl.SEPERATOR+payload.id,payload) : this.apiService.save(ApiUrls.CATEGORIES,payload);
  }

  saveCategoryTask(categoryID,payload:CategoryTaskRequest) {
    return payload.id ? this.apiService.update(ApiUrls.CATEGORIES+RouteUrl.SEPERATOR+categoryID+ApiUrls.TASKS+RouteUrl.SEPERATOR+payload.id,payload) : this.apiService.save(ApiUrls.CATEGORIES+RouteUrl.SEPERATOR+categoryID+ApiUrls.TASKS,payload);
  }

  saveTaskOption(categoryID,payload:TaskOptionRequest) {
    return payload.id ? this.apiService.update(ApiUrls.CATEGORIES+RouteUrl.SEPERATOR+categoryID+ApiUrls.OPTIONS+RouteUrl.SEPERATOR+payload.id,payload) : this.apiService.save(ApiUrls.CATEGORIES+RouteUrl.SEPERATOR+categoryID+ApiUrls.OPTIONS,payload);
  }

  saveFAQ(payload:FAQRequest) {
    return payload.id ? this.apiService.update(ApiUrls.FAQS+RouteUrl.SEPERATOR+payload.id,payload) : this.apiService.save(ApiUrls.FAQS,payload);
  }

  deleteCategoryTask(categoryID,taskID) {
    return this.apiService.delete(ApiUrls.CATEGORIES+RouteUrl.SEPERATOR+categoryID+ApiUrls.TASKS+RouteUrl.SEPERATOR+taskID);
  }

  deleteFAQ(categoryID: number, taskID: number, id: any) {
    return this.apiService.delete(ApiUrls.CATEGORIES+RouteUrl.SEPERATOR+categoryID+ApiUrls.TASKS+RouteUrl.SEPERATOR+taskID+ApiUrls.FAQS_API+RouteUrl.SEPERATOR+id);
  }

  deleteCategory(id) {
    return this.apiService.delete(ApiUrls.CATEGORIES+RouteUrl.SEPERATOR+id);
  }
}
