package com.src.proserv.main.exceptions;

import com.src.proserv.main.utils.RequestStatus;

public class UserRequestException extends AbstractRuntimeException {

	public UserRequestException(RequestStatus error,Object... msgParams) {
        super(error.getCode(),error.getDescription(msgParams));
    }
}
