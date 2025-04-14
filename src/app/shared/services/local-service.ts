import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {

  constructor() {}

  /**
   * Saves data to localStorage.
   * @param key - The key to store the data under
   * @param value - The value to store (will be stringified if it's an object)
   */
  public saveData(key: string, value: any): void {
    const data = typeof value === 'string' ? value : JSON.stringify(value);
    localStorage.setItem(key, data);
  }

  /**
   * Retrieves data from localStorage.
   * @param key - The key of the data to retrieve
   * @returns The parsed or raw data from localStorage, or null if not available
   */
  public getData<T>(key: string): T | null {
    const data = localStorage.getItem(key);
    try {
      return data ? JSON.parse(data) as T : null;
    } catch (error) {
      return data as unknown as T;
    }
  }

  /**
   * Removes data from localStorage.
   * @param key - The key of the data to remove
   */
  public deleteData(key: string): void {
    localStorage.removeItem(key);
  }


  public clearData() {
    localStorage.clear();
  }
}
