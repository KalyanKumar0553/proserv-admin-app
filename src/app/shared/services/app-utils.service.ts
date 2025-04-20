import { Injectable } from '@angular/core';
import { AppLoginComponent } from 'app/modules/auth/app-login/app-login.component';
import { MenuItem } from '../models/menu-item.model';

@Injectable({
    providedIn: 'root'
})
export class AppUtilsService {
    
    fetchActiveHeader(navItems: MenuItem[]): string {
        let activeHeader = 'home';
        for (let i = 0; i < navItems.length; i++) {
            let curNav = navItems[i];
            let currNavChild = curNav.children;
            for (let j = 0; j < currNavChild.length; j++) {
                let currMenuItem = currNavChild[j];
                if(currMenuItem.active) {
                    activeHeader = curNav.label;
                }
            }
        }
        return activeHeader;
    }

    fetchActiveComponentFromMenu(navItems: MenuItem[],defaultComponent: string): string {
        let component = defaultComponent;
        for (let i = 0; i < navItems.length; i++) {
            let curNav = navItems[i];
            let currNavChild = curNav.children;
            for (let j = 0; j < currNavChild.length; j++) {
                let currMenuItem = currNavChild[j];
                if(currMenuItem.active) {
                    component = curNav.route;
                }
            }
        }
        return component;
    }

    updateMenuExpansion(navItems: MenuItem[], activeLabel: string) {
        let isExpanded = false;
        for (let i = 0; i < navItems.length; i++) {
            let currNav = navItems[i];
            currNav.active = false;
            let currNavChild = navItems[i].children;
            for (let j = 0; j < currNavChild.length; j++) {
                let currMenuItem = currNavChild[j];
                currMenuItem.active = false;
                if (currMenuItem.route == activeLabel) {
                    isExpanded = true;
                    currMenuItem.active = true;
                    currNav.active = true;
                }
            }
        }
        if ((!isExpanded) && navItems.length > 0) {
            navItems[0].active = true;
            if (navItems[0].children.length > 0) {
                navItems[0].children[0].active = true;
            }
        }
    }
}