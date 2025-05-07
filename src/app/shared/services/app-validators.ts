import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

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