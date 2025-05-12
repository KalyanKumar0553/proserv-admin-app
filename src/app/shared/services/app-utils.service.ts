import { Injectable } from '@angular/core';
import { MenuItem } from '../models/menu-item.model';
import { filter, Subscription } from 'rxjs';
import { NavigationEnd, Router } from '@angular/router';
import { appConfig } from '../constants/app-config.enum';
import { FormGroup } from '@angular/forms';
@Injectable({
    providedIn: 'root'
})
export class AppUtilsService {

    constructor() { }

    fetchActiveHeader(navItems: MenuItem[]): string {
        let activeHeader = 'home';
        for (let i = 0; i < navItems.length; i++) {
            let currNav = navItems[i];
            let currNavChild = currNav?.children || [];
            if (currNavChild.length == 0 && currNav.active) {
                activeHeader = currNav.label;
                break;
            }
            for (let j = 0; j < currNavChild.length; j++) {
                let currMenuItem = currNavChild[j];
                if (currMenuItem.active) {
                    activeHeader = currNav.label;
                }
            }
        }
        return activeHeader;
    }

    fetchActiveComponentFromMenu(navItems: MenuItem[], defaultComponent: string = ''): string {
        let component:any = defaultComponent;
        let firstComponent = this.fetchDefaultComponentFromMenu(navItems);
        for (let i = 0; i < navItems.length; i++) {
            let currNav = navItems[i];
            let currNavChild = currNav?.children || [];
            if (currNavChild.length == 0 && currNav.active) {
                component = currNav.route;
                break;
            }
            for (let j = 0; j < currNavChild.length; j++) {
                let currMenuItem = currNavChild[j];
                if (currMenuItem.active) {
                    component = currNav.route;
                }
            }
        }
        return component || firstComponent;
    }

    fetchDefaultComponentFromMenu(navItems: MenuItem[]): string {
        let component:any = '';
        for (let i = 0; i < navItems.length; i++) {
            let currNav = navItems[i];
            let currNavChild = currNav?.children || [];
            if (currNavChild.length == 0 && currNav.active) {
                component = currNav.route;
                break;
            }
            if (currNavChild?.length > 0) {
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
            if (currNavChild.length == 0 && currNav.route == activeLabel) {
                isExpanded = true;
                currNav.active = true;
                // break;
            }
            for (let j = 0; j < currNavChild.length; j++) {
                let currMenuItem = currNavChild[j];
                currMenuItem.active = false;
                if (currMenuItem.route == activeLabel) {
                    isExpanded = true;
                    currMenuItem.active = true;
                    currNav.active = true;
                    // break;
                }
            }
            if (isExpanded) {
                break;
            }
        }
        if ((!isExpanded) && navItems.length > 0) {
            navItems[0].active = true;
            if (navItems?.[0]?.children?.length > 0) {
                navItems[0].children[0].active = true;
            }
        }
    }

    unsubscribeData(subscriptions: Subscription[]) {
        subscriptions.forEach((sub) => {
            if (sub) {
                sub.unsubscribe();
            }
        })
    }

    navigateToComponent(context:any,route: string, component: string) {
        context.router.navigate([route], {
            queryParams: { activeComponent: component }
        });
    }

    updateMenuOnRouteChange(context: any) {
        context.routerSubscription = context.router.events
            .pipe(filter(event => event instanceof NavigationEnd))
            .subscribe(() => {
                context.route.queryParams.subscribe(params => {
                    const activeComponent = params['activeComponent'];
                    if (activeComponent) {
                        context.activeComponent = activeComponent;
                        this.loadMenuBasedOnRoute(context, context.router.url);
                    }
                });
            });
    }

    loadMenuBasedOnRoute(context: any, route: string) {
        if (route.length > 1) {
            route = route.indexOf("/") == -1 ? route : route.substring(route.indexOf("/") + 1);
            route = route.split('?')[0];
            if (appConfig[route]) {
                let configData = appConfig[route];
                let navItems = configData.sideNavMenu;
                this.updateMenuExpansion(navItems, context.activeComponent);
                context.sideNavMenu = navItems;
                context.updateSignal++;
            } else {
                context.sideNavMenu = [];
                context.updateSignal++;
            }
        }
    }

    focusFirstInvalidControl(context: any, formGroup: FormGroup): void {
        const controls = formGroup.controls;
        const controlKeys = Object.keys(controls);
        const keysWithValidators = controlKeys.filter(key =>
            controls[key].validator !== null || controls[key].asyncValidator !== null
        );
        const firstInvalidKey = keysWithValidators.find(key => controls[key].invalid);
        if (firstInvalidKey) {
            const index = keysWithValidators.indexOf(firstInvalidKey); // Use original order for matching DOM elements
            const field = context.formFields.toArray()[index];
            field?.nativeElement?.focus();
        }
        return;
    }
    
}