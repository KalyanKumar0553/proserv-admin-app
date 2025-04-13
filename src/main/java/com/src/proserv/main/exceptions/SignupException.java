package com.src.proserv.main.exceptions;

import com.src.proserv.main.utils.RequestStatus;

public class SignupException extends AbstractRuntimeException {

	public SignupException(RequestStatus error,Object... msgParams) {
        super(error.getCode(),error.getDescription(msgParams));
    }
}
