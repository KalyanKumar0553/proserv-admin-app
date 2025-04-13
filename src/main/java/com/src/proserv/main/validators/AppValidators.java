package com.src.proserv.main.validators;

import java.util.Optional;
import java.util.regex.Pattern;

import com.src.proserv.main.utils.AppConstants;

public class AppValidators {

	public static boolean isMobile(String input) {
		return (!isEmpty(input)) && Pattern.matches(AppConstants.mobileRegex, input);
	}

	public static boolean isEmail(String input) {
		return (!isEmpty(input)) && Pattern.matches(AppConstants.emailRegex, input);
	}

	private static boolean isEmpty(String input) {
		Optional<String> nullableIinput = Optional.ofNullable(input);
		if(nullableIinput.isEmpty()) {
			return true;
		}
		if(input.isEmpty() || input.isBlank()) {
			return true;
		}
		return false;
	}
}
