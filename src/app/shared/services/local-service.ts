import { Injectable } from '@angular/core';
import { Preferences } from '@capacitor/preferences';

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {

  constructor() {}

  /**
   * Saves data using Capacitor Preferences.
   * @param key - The key to store the data under
   * @param value - The value to store (will be stringified if it's an object)
   */
  public async saveData(key: string, value: any): Promise<void> {
    const data = typeof value === 'string' ? value : JSON.stringify(value);
    await Preferences.set({ key, value: data });
  }

  /**
   * Retrieves data using Capacitor Preferences.
   * @param key - The key of the data to retrieve
   * @returns The parsed or raw data, or null if not available
   */
  public async getData<T>(key: string): Promise<T | null> {
    const { value } = await Preferences.get({ key });
    if (value === null) return null;

    try {
      return JSON.parse(value) as T;
    } catch {
      return value as unknown as T;
    }
  }

  /**
   * Removes a specific key-value pair.
   * @param key - The key to remove
   */
  public async deleteData(key: string): Promise<void> {
    await Preferences.remove({ key });
  }

  /**
   * Clears all stored data.
   */
  public async clearData(): Promise<void> {
    await Preferences.clear();
  }
}
