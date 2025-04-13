package com.src.proserv.main.exceptions;

import com.src.proserv.main.utils.RequestStatus;

public class OTPException extends AbstractRuntimeException {

	public OTPException(RequestStatus error,Object... msgParams) {
        super(error.getCode(),error.getDescription(msgParams));
    }
}
