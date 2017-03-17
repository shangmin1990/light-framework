package com.le.bigdata.auth.authentication.exception;

import com.le.bigdata.core.exception.BusinessServiceException;


/**
 * Created by benjamin on 9/12/14.
 */
public class NoGrantTypeFoundException extends BusinessServiceException {

    public NoGrantTypeFoundException(int code) {
        super(code);
    }

    public NoGrantTypeFoundException(int code, String message) {
        super(code, message);
    }

    public NoGrantTypeFoundException(int code, String message, Throwable throwable) {
        super(code, message, throwable);
    }
}
