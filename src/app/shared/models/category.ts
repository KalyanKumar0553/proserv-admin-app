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