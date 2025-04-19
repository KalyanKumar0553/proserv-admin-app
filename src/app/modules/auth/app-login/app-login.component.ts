import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AlertTypes, AuthStateConstants, LocalStorageKeys } from 'app/shared/constants/constants.enum';
import RouteUrl from 'app/shared/constants/router-url.enum';
import { AuthService } from 'app/shared/services/auth.service';
import { LocalStorageService } from 'app/shared/services/local-service';
import { AuthValidationService } from 'app/shared/services/auth-validation-service'
import { NavigationTrackerService } from 'app/shared/services/navigration-tracking.service';
import { DeviceDetectorService } from 'app/shared/services/device-detector.service';
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
  
  constructor(private deviceDetector : DeviceDetectorService,private route: ActivatedRoute,private router: Router,private validationService:AuthValidationService,private authService: AuthService,private localService : LocalStorageService) { }

  ngOnInit(): void {
    this.isMobile = this.deviceDetector.isMobileDevice();
    if(!this.isMobile) {
      this.username = "";
      this.loading = true;
      if(this.authService.getToken()!=null) {
        this.authService.ping().then(()=>{
          this.router.navigate(['/home'])
        }).catch(err=>{
          this.state = AuthStateConstants.LOGIN_STATE;
        }).finally(()=>{
          this.loading = false;
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
    this.authService.sendOTP({"username":this.username}).then(res=>{
      this.state = this.verifyOTPAndResetPasswordState;
    }).catch(err=>{
      this.errorMsg = err?.message || "Unable To Send OTP.";
    });
  }

  verifyOTPAndReset() {
    this.alertType = AlertTypes.ERROR;
    if(!this.validationService.validateResetPasswordWithOTPRequest(this)) {
        return;
    } else {
      this.authService.resetPasswordWithOTP({"username":this.username,"password":this.password,"retypePassword":this.retypePassword,"otp":this.otp}).then(res=>{
        this.validationService.resetFields(this,['username','password','retypePassword','otp','errorMsg']);
        // this.state = this.loginState;
        this.errorMsg = "Password Succesfullt Updated. Please try login."
        this.alertType = AlertTypes.SUCCESS;
      }).catch(err=>{
        this.errorMsg = err?.message || "Unable To Reset Password.";
      })
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
    this.alertType = AlertTypes.ERROR;
    if(!this.validationService.validateLoginRequest(this)) {
      return;
    }
    this.errorMsg = '';
    this.authService.loginUser({"username":this.username,"password":this.password}).then(res=>{
      let token = res?.statusMsg?.accessToken;
      if(res?.statusMsg?.accessToken) {
        this.authService.setUserSession(res?.statusMsg?.accessToken);
        this.router.navigateByUrl(RouteUrl.ROUTE_SEPARATOR+RouteUrl.HOME);
      }
    }).catch(err=>{
      this.errorMsg = err?.message || "Unable To login with credentials !";
    }); 
  }
}
