import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { AlertTypes, AuthStateConstants, LocalStorageKeys } from 'app/shared/constants/constants.enum';
import RouteUrl from 'app/shared/constants/router-url.enum';
import { AuthService } from 'app/shared/services/auth.service';
import { LocalStorageService } from 'app/shared/services/local-service';
import { AuthValidationService } from 'app/shared/services/auth-validation-service'
import { NavigationTrackerService } from 'app/shared/services/navigration-tracking.service';
import { DeviceDetectorService } from 'app/shared/services/device-detector.service';
import { filter, Subject, Subscription } from 'rxjs';
import { AppUtilsService } from 'app/shared/services/app-utils.service';
@Component({
  selector: 'app-login',
  templateUrl: './app-login.component.html',
  standalone: false,
  styleUrls: ['./app-login.component.scss']
})
export class AppLoginComponent implements OnInit, OnDestroy {

  @ViewChild('usernameInput') usernameInputRef!: ElementRef;
  @ViewChild('passwordInput') passwordInputRef!: ElementRef;
  @ViewChild('retypePasswordInput') retypePasswordInputRef!: ElementRef;
  @ViewChild('otpInput') otpInputRef!: ElementRef;

  loading:boolean = false;
  state: string = null;
  username:string = "";
  password: String = null;
  errorMsg = "";
  otp:string = "";
  retypePassword:string = "";
  submitted = false;
  rememberMe : boolean = false;
  alertType:string =  AlertTypes.ERROR
  loginState:string = AuthStateConstants.LOGIN_STATE;
  forgotPasswordState:string = AuthStateConstants.FORGOT_PASSWORD_STATE;
  verifyOTPAndResetPasswordState:string = AuthStateConstants.VERIFY_OTP_STATE;
  isMobile:boolean = false;
  AuthStateConstants: any = AuthStateConstants;
  requestProgress:boolean = false;

  private pingSubscription? : Subscription;
  private sendOTPSubscription? : Subscription;
  private resetOTPSubscription? : Subscription;
  private loginSubscription? : Subscription;

  constructor(private utils: AppUtilsService,private deviceDetector : DeviceDetectorService,private route: ActivatedRoute,private router: Router,private validationService:AuthValidationService,private authService: AuthService,private localService : LocalStorageService) { }
  
  ngOnDestroy(): void {
    this.utils.unsubscribeData([this.pingSubscription,this.sendOTPSubscription,this.resetOTPSubscription,this.loginSubscription]);
  }

  

  ngOnInit(): void {
    this.isMobile = this.deviceDetector.isMobileDevice();
    if(!this.isMobile) {
      this.username = "";
      this.loading = true;
      if(this.authService.getToken()!=null) {
        this.pingSubscription = this.authService.ping().subscribe((data)=>{
          this.loading = false;
          this.router.navigateByUrl(RouteUrl.HOME);
        },(err)=>{
          this.loading = false;
          this.state = AuthStateConstants.LOGIN_STATE;
        });
      } else {
        this.loading = false;
        this.state = AuthStateConstants.LOGIN_STATE;
      }
      this.route.queryParams.subscribe(params => {
        if (params['reason'] === 'session_expired') {
          this.errorMsg = 'Your session has expired. Please login again.';
        }
      });
    } else {
      this.loading = false;
    }
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


  sendOTP() {
    this.alertType = AlertTypes.ERROR;
    this.errorMsg = "";
    this.validationService.validateSendOTPRequest(this);
    this.requestProgress = true;
    this.sendOTPSubscription = this.authService.sendOTP({"username":this.username}).subscribe(res=>{
      this.state = this.verifyOTPAndResetPasswordState;
      this.requestProgress = false;
    },(err)=>{
      this.errorMsg = err?.message || "Unable To Send OTP.";
      this.requestProgress = false;
    });
  }

  verifyOTPAndReset() {
    this.alertType = AlertTypes.ERROR;
    if(!this.validationService.validateResetPasswordWithOTPRequest(this)) {
        return;
    } else {
      this.requestProgress = true;
      this.resetOTPSubscription = this.authService.resetPasswordWithOTP({"username":this.username,"password":this.password,"retypePassword":this.retypePassword,"otp":this.otp}).subscribe(res=>{
        this.validationService.resetFields(this,['username','password','retypePassword','otp','errorMsg']);
        this.errorMsg = "Password Succesfully Updated. Please try login."
        this.alertType = AlertTypes.SUCCESS;
        this.requestProgress = false;
      },(err=>{
        this.errorMsg = err?.message || "Unable To Reset Password.";
        this.requestProgress = false;
      }));
    }
  }

  showForgotPasswordScreen() {
    this.validationService.resetFields(this,['username','errorMsg','otp']);
    this.state = this.forgotPasswordState;
  }

  showLoginScreen() {
    this.validationService.resetFields(this,['username','password','otp','errorMsg']);
    this.state = this.loginState;
  }

  login(event:any) {
    this.localService.clearData();
    this.alertType = AlertTypes.ERROR;
    if(!this.validationService.validateLoginRequest(this)) {
      return;
    }
    this.errorMsg = '';
    this.requestProgress = true;
    this.loginSubscription = this.authService.loginUser({"username":this.username,"password":this.password}).subscribe(res=>{
      let token = res?.statusMsg?.accessToken;
      if(res?.statusMsg?.accessToken) {
        this.authService.setUserSession(res?.statusMsg?.accessToken);
        this.router.navigateByUrl(RouteUrl.HOME);
      }
      this.requestProgress = false;
    },(err=>{
      this.errorMsg = err?.message || "Unable To login with credentials !";
      this.requestProgress = false;
    }));
  }
}
