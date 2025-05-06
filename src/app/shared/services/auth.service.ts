import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import { LocalStorageService } from './local-service';
import { ApiUrls } from '../constants/constants.enum';
import { ProServApiService } from './proserv-api.service';
import {jwtDecode} from 'jwt-decode';
import { JwtPayload } from '../models/jwt-payload.model';
import { Roles } from '../models/roles.enum';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private currentUser: { roles: Roles[]; token: string; exp: number } | null = null;
  private checkInterval = 10000;

  public clearDataTimer: any;

  constructor(private apiService:ProServApiService,private router: Router,private localService: LocalStorageService){
    this.restoreSession();
  }

  logoutUser() {
    this.clearTimer();
    return this.apiService.save(ApiUrls.LOGOUT,{});
  }

  loginUser(payload:any={}) {
    return this.apiService.save(ApiUrls.LOGIN,payload);
  }

  resetPasswordWithoutOTP(payload:any={}) {
    return this.apiService.save(ApiUrls.RESET_PASSWORD_WITHOOUT_OTP,payload);
  }

  resetPasswordWithOTP(payload:any={}) {
    return this.apiService.save(ApiUrls.RESET_PASSWORD_WITH_OTP,payload);
  }

  sendOTP(payload:any={}) {
    return this.apiService.save(ApiUrls.SEND_OTP,payload);
  }

  fetchRoles() {
    return this.apiService.get(ApiUrls.ROLES);
  }

  ping(): Observable<any>  {
    return this.apiService.get(ApiUrls.PING);
  }

  clearTimer() {
    if(this.clearDataTimer) {
      clearTimeout(this.clearDataTimer);
    }
  }

  getUser(): { roles: Roles[]; token: string } | null {
    if (!this.currentUser) {
      const stored = localStorage.getItem('user');
      this.currentUser = stored ? JSON.parse(stored) : null;
    }
    return this.currentUser;
  }

  getRoles(): Roles[] {
    return this.getUser()?.roles || [];
  }

  hasRole(role: Roles): boolean {
    return this.getRoles().includes(role);
  }

  hasAnyRole(rolesToCheck: Roles[]): boolean {
    return this.getRoles().some(r => rolesToCheck.includes(r));
  }

  getToken(): string | null {
    return this.getUser()?.token || null;
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  setUserSession(token: string) {
    const decoded = jwtDecode<JwtPayload>(token);
    this.currentUser = { token, roles: decoded.roles, exp: decoded.exp };
    localStorage.setItem('user', JSON.stringify(this.currentUser));
  }

  private restoreSession() {
    const stored = localStorage.getItem('user');
    if (stored) {
      this.currentUser = JSON.parse(stored);
    }
  }

  clearUserData() {
    this.currentUser = null;
  }
}
