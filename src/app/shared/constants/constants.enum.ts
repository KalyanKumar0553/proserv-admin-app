
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
  LOGIN = '/auth/login',
  SEND_OTP = '/auth/send_otp',
  RESET_PASSWORD_WITHOOUT_OTP = '/auth/reset_password_without_otp',
  RESET_PASSWORD_WITH_OTP = '/auth/reset_password_with_otp',
  ROLES = '/auth/roles',
  PING = '/ping',
  OVERVIEW = '/overview',
  LIST_CATEGORY = '/service/categories',
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