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