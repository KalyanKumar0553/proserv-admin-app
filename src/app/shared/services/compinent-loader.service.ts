// src/app/services/menu.service.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { MenuItem } from '../models/menu-item.model';

@Injectable({ providedIn: 'root' })
export class ComponentLoaderService {
  
  private componentSubject = new BehaviorSubject<string>('');

  component$: Observable<string> = this.componentSubject.asObservable();

  setComponent(component: string): void {
    this.componentSubject.next(component);
  }

  clearComponent(): void {
    this.componentSubject.next('');
  }
}
