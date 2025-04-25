import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import { LocalStorageService } from './local-service';
import { ApiUrls } from '../constants/constants.enum';
import { ProServApiService } from './proserv-api.service';
import {jwtDecode} from 'jwt-decode';
import { JwtPayload } from '../models/jwt-payload.model';
import { Roles } from '../models/roles.enum';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private currentUser: { roles: Roles[]; token: string; exp: number } | null = null;
  private checkInterval = 10000;

  public clearDataTimer: any;

  constructor(private apiService:ProServApiService,private router: Router,private localService: LocalStorageService){
  }

  listCategories() {
    return this.apiService.get(ApiUrls.LIST_CATEGORY).toPromise();
  }

}
