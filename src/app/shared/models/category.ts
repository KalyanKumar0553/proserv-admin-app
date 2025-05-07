export interface Category {
  id: number;
  name: string;
  providers: number;
  locations: number;
}
export interface CreateCategoryRequest {
  name: string;
  displayURL?: string;
  enabled: boolean;
}
export interface UpdateCategoryRequest {
  id:string;
  name: string;
  displayURL?: string;
  enabled: boolean;
}
export interface CreateCategoryTaskRequest {
    serviceCategoryID:number;
    title:string;
    displayURL:string;
    description:string;
    inclusions?:string;
    exclusions?:string;
    note?:string;
    enabled:boolean;
}
export interface UpdateCategoryTaskRequest {
  id:number;
  serviceCategoryID:number;
  title:string;
  displayURL:string;
  description:string;
  inclusions?:string;
  exclusions?:string;
  note?:string;
  enabled:boolean;
}
export interface CategoryTask {
  id?:number;
  title?:string;
  description?:string;
  serviceCategoryID?:number;
  inclusions?:string;
  exclusions?:string;
  displayURL?:string;
  note?:string;
  enabled?:boolean;
}
export interface CreateFAQRequest {
  question:string;
  answer:string;
  serviceTaskID:number;
  serviceCategoryID:number;
}