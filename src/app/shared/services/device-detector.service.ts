import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DeviceDetectorService {
  isMobileDevice(): boolean {
    const userAgent = navigator.userAgent || navigator.vendor || (window as any).opera;
    let isMobile = /android|iphone|ipad|iPod|opera mini|iemobile|mobile/i.test(userAgent)
    return isMobile;
  }
}
