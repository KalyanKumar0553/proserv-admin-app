// src/app/services/component-config.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ComponentConfigService {
  
  fileConfig : any = {
     'categories' : 'categories-config'
  };

  constructor(private http: HttpClient) {}

  async loadConfigFile(name: string): Promise<any> {
    const url = `/assets/configuration/${name}.json`;
    try {
      return await firstValueFrom(this.http.get<any>(url));
    } catch (error) {
      console.error(`Failed to load config for ${name}:`, error);
      return null;
    }
  }

  getFileConfig() {
    return this.fileConfig;
  }
}
