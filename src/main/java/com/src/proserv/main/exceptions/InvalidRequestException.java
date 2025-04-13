package com.src.proserv.main.exceptions;

import com.src.proserv.main.utils.RequestStatus;

public class InvalidRequestException extends AbstractRuntimeException {

	public InvalidRequestException(RequestStatus error,Object... msgParams) {
        super(error.getCode(),error.getDescription(msgParams));
    }
}
