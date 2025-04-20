// src/app/services/config.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ConfigService {
  private config: any;

  constructor(private http: HttpClient) {}

  loadConfig(): Promise<void> {
    return firstValueFrom(this.http.get('/assets/configuration/configuration.json'))
      .then(data => {
        this.config = data;
      });
  }

  get(key: string): any {
    return this.config?.[key];
  }

  getAll(): any {
    return this.config;
  }
}
