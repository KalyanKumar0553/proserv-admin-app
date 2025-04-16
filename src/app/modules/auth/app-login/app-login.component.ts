import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { AuthStateConstants } from 'app/shared/constants/constants.enum';
import RouteUrl from 'app/shared/constants/router-url.enum';
import { AuthService } from 'app/shared/services/auth.service';
import { ValidationService } from 'app/shared/services/validation-service'
@Component({
  selector: 'app-login',
  templateUrl: './app-login.component.html',
  standalone: false,
  styleUrls: ['./app-login.component.scss']
})
export class AppLoginComponent implements OnInit {

  @ViewChild('usernameInput') usernameInputRef!: ElementRef;
  @ViewChild('passwordInput') passwordInputRef!: ElementRef;
  @ViewChild('retypePasswordInput') retypePasswordInputRef!: ElementRef;
  @ViewChild('otpInput') otpInputRef!: ElementRef;

  state: string = AuthStateConstants.LOGIN_STATE;
  username:string = "";
  password: String = null;
  errorMsg = "";
  otp:string = "";
  retypePassword:string = "";
  submitted = false;

  loginState:string = AuthStateConstants.LOGIN_STATE;
  forgotPasswordState:string = AuthStateConstants.FORGOT_PASSWORD_STATE;
  verifyOTPAndResetPasswordState:string = AuthStateConstants.VERIFY_OTP_STATE;
  
  AuthStateConstants: any = AuthStateConstants;
  
  constructor(private router: Router,private validationService:ValidationService,private authService: AuthService) { }

  ngOnInit(): void {
    this.username = "";
    this.state = AuthStateConstants.LOGIN_STATE;
  }

  updateStateToForgotPassword() {
    this.username = "";
    this.password = "";
    this.errorMsg = "";
    this.state = AuthStateConstants.FORGOT_PASSWORD_STATE;
  }
  
  updateStateToLogin() {
    this.username = "";
    this.errorMsg = "";
    this.state = AuthStateConstants.LOGIN_STATE;
  }

  login() {
    this.errorMsg = "";
    if(!this.username) {
      this.errorMsg = "Please enter email !";
    } else  if(!this.password) {
      this.errorMsg = "Please enter password !";
    }else if(!this.validationService.validateEmail(this.username)) {
      this.errorMsg = "Please enter valid email !";
    } else {
      this.authService.loginUser({"username":this.username,"password":this.password}).then(res=>{
          this.navigateToDashboard();
      }).catch(err=>{
        this.errorMsg = "Unable To login with credentials !";
      }); 
    }
  }

  navigateToDashboard() {
    this.router.navigateByUrl(RouteUrl.ROUTE_SEPARATOR+RouteUrl.ADMIN_DASHBOARD);
  }

  resetPassword() {
    this.errorMsg = "";
    if(!this.username) {
      this.errorMsg = "Please enter email !";
    } else if(!this.validationService.validateEmail(this.username)) {
      this.errorMsg = "Please enter valid email !";
    } else {
      let result = this.authService.resetPassword({"email":this.username,"isEmailSent":true});
    }
  }


  sendOTP() {
    this.errorMsg = "";
    if (!this.username) {
      this.errorMsg = '* All fields are required.';
      if(!this.username) {
        this.usernameInputRef.nativeElement.focus();
        return;
      }
    }
    if(!this.isValidEmailOrPhone(this.username)) {
      this.errorMsg = "* Please enter valid email or phone number";
      this.usernameInputRef.nativeElement.focus();
      return;
    }
    this.state = this.verifyOTPAndResetPasswordState;
  }

  verifyOTPAndReset() {
    if (!this.username || !this.password || !this.otp || !this.retypePassword) {
      this.errorMsg = '* All fields are required.';
      if(!this.username) {
        this.usernameInputRef.nativeElement.focus();
        return;
      }
      if(!this.otp) {
        this.otpInputRef.nativeElement.focus();
        return;
      }
      if(!this.password) {
        this.passwordInputRef.nativeElement.focus();
        return;
      }
      if(!this.retypePassword) {
        this.retypePasswordInputRef.nativeElement.focus();
        return;
      }
    }
    if(this.otp.length!=6) {
      this.errorMsg = "* Please enter valid OTP";
      this.otpInputRef.nativeElement.focus();
      return;
    }
    if(this.password != this.retypePassword) {
      this.errorMsg = "* Password doesn't match";
      this.retypePasswordInputRef.nativeElement.focus();
      return;
    }
    this.username = "";
    this.password = "";
    this.retypePassword = "";
    this.otp = "";
    this.errorMsg = "";
    this.state = this.loginState;
  }

  showForgotPasswordScreen() {
    this.username = "";
    this.errorMsg = "";
    this.otp = "";
    this.state = this.forgotPasswordState;
  }

  showLoginScreen() {
    this.username = "";
    this.password = "";
    this.errorMsg = "";
    this.otp = "";
    this.state = this.loginState;
  }

  signinUser() {
    if (!this.username || !this.password) {
      this.errorMsg = '* All fields are required.';
      if(!this.username) {
        this.usernameInputRef.nativeElement.focus();
        return;
      }
      if(!this.password) {
        this.passwordInputRef.nativeElement.focus();
        return;
      }
      return;
    }
    if(!this.isValidEmailOrPhone(this.username)) {
      this.errorMsg = "* Please enter valid email or phone number";
      this.usernameInputRef.nativeElement.focus();
      return;
    }
    this.errorMsg = '';
  }

  isValidEmailOrPhone(value: string): boolean {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const phonePattern = /^\d{10}$/;
    return emailPattern.test(value) || phonePattern.test(value);
  }
}
