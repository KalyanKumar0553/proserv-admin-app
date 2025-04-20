// src/app/services/menu.service.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { MenuItem } from '../models/menu-item.model';

@Injectable({ providedIn: 'root' })
export class MenuService {
  private menuItemsSubject = new BehaviorSubject<MenuItem[]>([]);

  menuItems$: Observable<MenuItem[]> = this.menuItemsSubject.asObservable();

  setMenu(items: MenuItem[]): void {
    this.menuItemsSubject.next(items);
  }

  addMenuItems(items: MenuItem[]): void {
    const current = this.menuItemsSubject.value;
    this.menuItemsSubject.next([...current, ...items]);
  }

  clearMenu(): void {
    this.menuItemsSubject.next([]);
  }
}
