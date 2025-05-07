import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import { LocalStorageService } from './local-service';
import { ApiUrls } from '../constants/constants.enum';
import { ProServApiService } from './proserv-api.service';
import {jwtDecode} from 'jwt-decode';
import { JwtPayload } from '../models/jwt-payload.model';
import { Roles } from '../models/roles.enum';
import { CreateCategoryRequest, CreateCategoryTaskRequest, UpdateCategoryRequest, UpdateCategoryTaskRequest } from '../models/category';
import RouteUrl from '../constants/router-url.enum';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private currentUser: { roles: Roles[]; token: string; exp: number } | null = null;
  private checkInterval = 10000;

  public clearDataTimer: any;

  constructor(private apiService:ProServApiService,private router: Router,private localService: LocalStorageService){
  }

  listCategories() {
    return this.apiService.get(ApiUrls.CATEGORIES);
  }

  deleteCategory(id) {
    return this.apiService.delete(ApiUrls.CATEGORIES+RouteUrl.SEPERATOR+id);
  }

  getCategory(id:any) {
    return this.apiService.get(ApiUrls.CATEGORIES+RouteUrl.SEPERATOR+id);
  }

  getCategoryTasks(categoryID:any) {
    return this.apiService.get(ApiUrls.CATEGORIES+RouteUrl.SEPERATOR+categoryID+ApiUrls.TASKS);
  }

  getCategoryTaskByID(categoryID:any,taskID:any) {
    return this.apiService.get(ApiUrls.CATEGORIES+RouteUrl.SEPERATOR+categoryID+ApiUrls.TASKS+RouteUrl.SEPERATOR+taskID);
  }

  getFAQByCategoryIDAndTaskID(categoryID:any,taskID:any) {
    return this.apiService.get(ApiUrls.CATEGORIES+RouteUrl.SEPERATOR+categoryID+ApiUrls.TASKS);
  }

  saveCategory(payload:CreateCategoryRequest) {
    return this.apiService.save(ApiUrls.CATEGORIES,payload);
  }

  updateCategory(id,payload:UpdateCategoryRequest) {
    return this.apiService.update(ApiUrls.CATEGORIES+RouteUrl.SEPERATOR+id,payload);
  }

  updateCategoryTask(categoryID,taskID,payload:UpdateCategoryTaskRequest) {
    return this.apiService.update(ApiUrls.CATEGORIES+RouteUrl.SEPERATOR+categoryID+ApiUrls.TASKS+RouteUrl.SEPERATOR+taskID,payload);
  }

  saveCategoryTask(categoryID,payload:CreateCategoryTaskRequest) {
    return this.apiService.save(ApiUrls.CATEGORIES+RouteUrl.SEPERATOR+categoryID+ApiUrls.TASKS,payload);
  }

  deleteCategoryTask(categoryID,taskID) {
    return this.apiService.delete(ApiUrls.CATEGORIES+RouteUrl.SEPERATOR+categoryID+ApiUrls.TASKS+RouteUrl.SEPERATOR+taskID);
  }
}
