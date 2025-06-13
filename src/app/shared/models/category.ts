export interface Category {
  id: number;
  name: string;
  providers: number;
  locations: number;
}
export interface CategoryRequest {
  id?:string;
  name: string;
  displayURL?: string;
  enabled: boolean;
}
export interface CategoryTaskRequest {
    id?:number;
    serviceCategoryID:number;
    title:string;
    displayURL:string;
    description:string;
    inclusions?:string;
    exclusions?:string;
    note?:string;
    enabled:boolean;
}
export interface TaskOptionRequest {
  id?:number;
  name:string;
  description:string;
  defaultAmount:number;
  taskDuration:number;
  inclusions:string;
  exclusions:string;
  serviceTaskID:number;
  serviceCategoryID:number;
}
export interface FAQRequest {
  id?:number;
  question:string;
  answer:string;
  serviceTaskID:number;
  serviceCategoryID:number;
}
