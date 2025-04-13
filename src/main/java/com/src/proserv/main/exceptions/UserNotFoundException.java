package com.src.proserv.main.exceptions;

import com.src.proserv.main.utils.RequestStatus;

public class UserNotFoundException extends AbstractRuntimeException {

	public UserNotFoundException(RequestStatus error,Object... msgParams) {
        super(error.getCode(),error.getDescription(msgParams));
    }
}
