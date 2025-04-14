import { Injectable } from '@angular/core';
import { LocalStorageService } from './local-service';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {

  private tokenKey = 'accessToken';

  constructor(private localStorageService: LocalStorageService) {}

  /**
   * Saves the encoded token to localStorage using LocalStorageService.
   * @param token - The access token to encode and store
   */
  public saveToken(token: string): void {
    const encodedToken = btoa(token); // Encode the token using Base64
    this.localStorageService.saveData(this.tokenKey, encodedToken);
  }

  /**
   * Retrieves and decodes the token from localStorage using LocalStorageService.
   * @returns The decoded access token or null if not available
   */
  public getToken(): string | null {
    const encodedToken = this.localStorageService.getData<string>(this.tokenKey);
    return encodedToken ? atob(encodedToken) : null; // Decode the token if it exists
  }

  /**
   * Removes the token from localStorage using LocalStorageService.
   */
  public clearToken(): void {
    this.localStorageService.deleteData(this.tokenKey);
  }
}
