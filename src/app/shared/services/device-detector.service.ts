import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DeviceDetectorService {
  isMobileDevice(): boolean {
    return window.innerWidth<560;
  }
}
