
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
  SEND_OTP = '/auth/send_otp'
}