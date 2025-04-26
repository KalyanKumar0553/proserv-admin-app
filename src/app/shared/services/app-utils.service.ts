import { Injectable } from '@angular/core';
import { AppLoginComponent } from 'app/modules/auth/app-login/app-login.component';
import { MenuItem } from '../models/menu-item.model';
import { Subscription } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AppUtilsService {
    
    fetchActiveHeader(navItems: MenuItem[]): string {
        let activeHeader = 'home';
        for (let i = 0; i < navItems.length; i++) {
            let currNav = navItems[i];
            let currNavChild = currNav?.children || [];
            if(currNavChild.length == 0 && currNav.active) {
                activeHeader = currNav.label;
                break;
            }
            for (let j = 0; j < currNavChild.length; j++) {
                let currMenuItem = currNavChild[j];
                if(currMenuItem.active) {
                    activeHeader = currNav.label;
                }
            }
        }
        return activeHeader;
    }

    fetchActiveComponentFromMenu(navItems: MenuItem[],defaultComponent: string=''): string {
        let component = defaultComponent;
        let firstComponent = this.fetchDefaultComponentFromMenu(navItems);
        for (let i = 0; i < navItems.length; i++) {
            let currNav = navItems[i];
            let currNavChild = currNav?.children || [];
            if(currNavChild.length == 0 && currNav.active) {
                component = currNav.route;
                break;
            }
            for (let j = 0; j < currNavChild.length; j++) {
                let currMenuItem = currNavChild[j];
                if(currMenuItem.active) {
                    component = currNav.route;
                }
            }
        }
        return component || firstComponent;
    }

    fetchDefaultComponentFromMenu(navItems: MenuItem[]) : string {
        let component = '';
        for (let i = 0; i < navItems.length; i++) {
            let currNav = navItems[i];
            let currNavChild = currNav?.children || [];
            if(currNavChild.length == 0 && currNav.active) {
                component = currNav.route;
                break;
            }
            if(currNavChild?.length>0) {
                component = currNavChild[0].route;
            }
        }
        return component;
    }


    updateMenuExpansion(navItems: MenuItem[], activeLabel: string) {
        let isExpanded = false;
        for (let i = 0; i < navItems.length; i++) {
            let currNav = navItems[i];
            currNav.active = false;
            let currNavChild = navItems[i]?.children || [];
            if(currNavChild.length == 0 && currNav.route==activeLabel) {
                isExpanded = true;
                currNav.active = true;
                break;
            }
            for (let j = 0; j < currNavChild.length; j++) {
                let currMenuItem = currNavChild[j];
                currMenuItem.active = false;
                if (currMenuItem.route == activeLabel) {
                    isExpanded = true;
                    currMenuItem.active = true;
                    currNav.active = true;
                    break;
                }
            }
            if(isExpanded) {
                break;
            }
        }
        if ((!isExpanded) && navItems.length > 0) {
            navItems[0].active = true;
            if (navItems[0].children.length > 0) {
                navItems[0].children[0].active = true;
            }
        }
    }

    unsubscribeData(subscriptions:Subscription[]) {
        subscriptions.forEach((sub)=>{
          if(sub) {
            sub.unsubscribe();
          }
        })
    }
}