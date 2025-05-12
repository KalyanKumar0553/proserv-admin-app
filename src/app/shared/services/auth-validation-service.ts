import { Injectable } from '@angular/core';
import { AppLoginComponent } from '../../modules/auth/app-login/app-login.component';

@Injectable({
  providedIn: 'root'
})
export class AuthValidationService {
  
  validateSendOTPRequest(context: any = {}) {
    if (!context.username) {
      context.errorMsg = '* All fields are required.';
      if(!context.username) {
        context.usernameInputRef.nativeElement.focus();
        return false;
      }
    }
    if(!this.isValidEmailOrPhone(context.username)) {
      context.errorMsg = "* Please enter valid email or phone number";
      context.usernameInputRef.nativeElement.focus();
      return false;
    }
    return true;
  }
  
  validateResetPasswordWithOTPRequest(context: any) : boolean {
    if(!context.username) {
        context.errorMsg = 'Username is required.';
        return false;
    }
    if(!context.password) {
        context.errorMsg = 'Password is required.';
        return false;
    }
    if(!this.isValidEmailOrPhone(context.username)) {
      context.errorMsg = "Please enter valid email or phone number";
      context.usernameInputRef.nativeElement.focus();
      return false;
    }
    if(!context.retypePassword) {
      context.retypePassword.nativeElement.focus();
      return false;
    }
    if(context.password != context.retypePassword) {
      context.errorMsg = "* Password doesn't match";
      context.retypePasswordInputRef.nativeElement.focus();
      return false;
    }
    if(!context.otp) {
      context.errorMsg = "* OTP is Required";
      context.otpInputRef.nativeElement.focus();
      return false;
    }
    if(context.otp.length!=6) {
      context.errorMsg = "* Please enter valid OTP";
      context.otpInputRef.nativeElement.focus();
      return false;
    }
    return true;
  }
  
  validateLoginRequest(context: any) : boolean {
    context.errorMsg = '';
    if(!this.validateUsernameAndPassword(context)) {
      return false;
    }
    return true;
  }

  resetFields(context:any={},fields:string[]) {
    fields.forEach(currField=>{
      context[currField] = '';
    });
  }

  validateUsernameAndPassword(context: any) : boolean {
    if (!context.username || !context.password) {
      if(!context.username) {
        context.errorMsg = 'Username is required.';
        context.usernameInputRef.nativeElement.focus();
        return false;
      }
      if(!context.password) {
        context.errorMsg = 'Password is required.';
        context.passwordInputRef.nativeElement.focus();
        return false;
      }
      return false;
    }
    if(!this.isValidEmailOrPhone(context.username)) {
      context.errorMsg = "Please enter valid email or phone number";
      context.usernameInputRef.nativeElement.focus();
      return false;
    }
    return true;
  }

  isValidEmailOrPhone(value: string): boolean {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const phonePattern = /^\d{10}$/;
    return emailPattern.test(value) || phonePattern.test(value);
  }

  validateEmail(input: string): boolean {
    const emailPattern = /^[A-Za-z0-9][A-Za-z0-9+_.-]*@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
    return emailPattern.test(input);
  }


}