import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import { LocalStorageService } from './local-service';
import { ApiUrls } from '../constants/constants.enum';
import { ProServApiService } from './proserv-api.service';

@Injectable({
  providedIn: 'root'
})
export class OverviewService {

  constructor(private apiService:ProServApiService,private router: Router,private localService: LocalStorageService){
  }
  
  async getOverviewData() {
    return this.apiService.get(ApiUrls.OVERVIEW).toPromise();
  }

  async getSideNavMenu() {
    
  }
}
