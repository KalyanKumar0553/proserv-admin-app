import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function alphaNumericSpaceSpecialValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        const value = control.value;

        if (value == null || value === '') {
            return null;
        }
        const regex = /^[a-zA-Z0-9 ?.,!&-]*$/;
        if (!regex.test(value)) {
            return { invalidCharacters: 'Only alphanumeric characters, spaces, and ?, . ! - & are allowed.' };
        }
        return null;
    };
}
export function alphaNumericSpaceValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        const value = control.value;

        if (value == null || value === '') {
            return null;
        }
        const regex = /^[a-zA-Z0-9 ]*$/;
        if (!regex.test(value)) {
            return { invalidCharacters: 'Only alphanumeric characters, and spaces are allowed' };
        }
        return null;
    };
}
export function alphabetSpaceValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        const value = control.value;

        if (value == null || value === '') {
            return null;
        }
        const regex = /^[a-zA-Z ]*$/;
        if (!regex.test(value)) {
            return { invalidCharacters: 'Only Alphabets and Spaces are allowed' };
        }
        return null;
    };
}
export function atLeastOneAlphabetValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        const value = control.value;
        const hasAlphabet = /[a-zA-Z]/.test(value);
        return !hasAlphabet ? { noAlphabet: true } : null;
    };
}
export function startsWithAlphabetValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
        const value = control.value;
        const startsWithAlphabet = /^[a-zA-Z]/.test(value);
        return !startsWithAlphabet ? { doesNotStartWithAlphabet: true } : null;
    };
}
export function minimumAmountValidator(minAmount: number = 10): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (value == null || value === '') {
        return null;
      }
      const amount = parseFloat(value);
      if (isNaN(amount)) {
        return { invalidAmount: 'Amount must be a valid number' };
      }
      if (amount < minAmount) {
        return { minAmount: `Minimum amount should be ${minAmount}` };
      }
      return null;
    };
}