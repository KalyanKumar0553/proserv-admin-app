import { RouteInfo } from "../models/route-info-dto";

export const dashboardRoute:RouteInfo = { path: '/admin/dashboard', title: 'Dashboard',  icon: 'dashboard', class: ''};
export const complexRoute : RouteInfo[] = [
  { path: '/user-profile', title: 'User Profile',  icon:'person', class: ''},
  { path: '/dashboard', title: 'Dashboard',  icon: 'dashboard', class: ''},
  { path: '/user-profile', title: 'User Profile',  icon:'person', class: ''},
  { path: '/dashboard', title: 'Dashboard',  icon: 'dashboard', class: ''},
  { path: '/user-profile', title: 'User Profile',  icon:'person', class: ''},
  { path: '/dashboard', title: 'Dashboard',  icon: 'dashboard', class: ''},
  { path: '/user-profile', title: 'User Profile',  icon:'person', class: ''}
];
export const directoryRoute : RouteInfo[] = [
  { path: '/user-profile', title: 'User Profile',  icon:'person', class: ''},
  { path: '/user-profile', title: 'User Profile',  icon:'person', class: ''},
  { path: '/dashboard', title: 'Dashboard',  icon: 'dashboard', class: ''},
  { path: '/user-profile', title: 'User Profile',  icon:'person', class: ''},
  { path: '/dashboard', title: 'Dashboard',  icon: 'dashboard', class: ''},
  { path: '/user-profile', title: 'User Profile',  icon:'person', class: ''}
];
export const gateKeeperRoute : RouteInfo[] = [
  { path: '/user-profile', title: 'User Profile',  icon:'person', class: ''},
  { path: '/dashboard', title: 'Dashboard',  icon: 'dashboard', class: ''},
  { path: '/user-profile', title: 'User Profile',  icon:'person', class: ''},
  { path: '/dashboard', title: 'Dashboard',  icon: 'dashboard', class: ''},
  { path: '/user-profile', title: 'User Profile',  icon:'person', class: ''},
];
export const facilitiesRoute : RouteInfo[] = [
  { path: '/user-profile', title: 'User Profile',  icon:'person', class: ''},
  { path: '/dashboard', title: 'Dashboard',  icon: 'dashboard', class: ''},
];
export const billingRoute : RouteInfo[] = [
  { path: '/user-profile', title: 'User Profile',  icon:'person', class: ''},
  { path: '/dashboard', title: 'Dashboard',  icon: 'dashboard', class: ''}
];
export const vendorRoute:RouteInfo = { path: '/user-profile', title: 'User Profile',  icon:'person', class: ''};
export const reportsRoute:RouteInfo = { path: '/user-profile', title: 'User Profile',  icon:'person', class: ''};
export const inventoryRoute:RouteInfo = { path: '/user-profile', title: 'User Profile',  icon:'person', class: ''};
export const settingsRoute : RouteInfo[] = [
  { path: '/user-profile', title: 'User Profile',  icon:'person', class: ''},
  { path: '/dashboard', title: 'Dashboard',  icon: 'dashboard', class: ''}
];

export const ROUTES: RouteInfo[] = [
  dashboardRoute,
  vendorRoute,
  reportsRoute,
  ...settingsRoute,
  ...billingRoute,
  ...gateKeeperRoute,
  ...directoryRoute,
  ...complexRoute,
  ...facilitiesRoute,
  inventoryRoute
]
  