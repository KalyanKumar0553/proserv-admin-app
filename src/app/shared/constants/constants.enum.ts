import RouteUrl from '../constants/router-url.enum';
import { BreadCrumbItem } from '../models/bread-crumb-item.model';

export enum AuthStateConstants {
  LOGIN_STATE = "auth_login",
  FORGOT_PASSWORD_STATE = "auth_forgot_password",
  VERIFY_OTP_STATE = "auth_verify_reset_otp"
}
export enum ResponseErrors {
  NETWORK_DISCONNECTED = "Could not connect to internet, check your network connections",
}
export enum ApiUrls {
  LOGOUT = '/auth/logout',
  REFRESH_TOKEN = '/auth/refresh-token',
  LOGIN = '/auth/login',
  SEND_OTP = '/auth/send_otp',
  RESET_PASSWORD_WITHOOUT_OTP = '/auth/reset_password_without_otp',
  RESET_PASSWORD_WITH_OTP = '/auth/reset_password_with_otp',
  ROLES = '/auth/roles',
  PING = '/ping',
  OVERVIEW = '/overview',
  CATEGORIES = '/service/categories',
  FAQS = '/service/faqs',
  TASKS = '/tasks',
  OPTIONS = '/options',
  FAQS_API = '/faqs',
}
export enum LocalStorageKeys {
   USER_TOKEN = 'TOKEN'
}
export enum AlertTypes {
  INFO = 'alert-info',
  SUCCESS = 'alert-success',
  ERROR = 'alert-danger',
  WARN = 'alert-warning'
}
export enum ComponentState {
  ADD = 'add',
  UPDATE = 'update'
}
export enum STATUS {
  SUCCESS = 'success',
  ERROR = 'error',
  INFO = 'info',
  WARN = 'warning'
}

export const CrumbItems : Record<string, BreadCrumbItem[]> = {
  AddCategoryCrumbItems : [
      { 'label': 'Home', 'route': RouteUrl.DASHBOARD, 'component': RouteUrl.DASHBOARD},
      { 'label': 'Categories' },
      { 'label': 'Add Category', 'isTail': true },
  ],
  UpdateCategoryCrumbItems : [
    {'label' : 'Home' , 'route' : RouteUrl.DASHBOARD, 'component':RouteUrl.DASHBOARD},
    {'label' : 'Categories'},
    {'label' : 'Update Category','isTail':true},
  ]
}