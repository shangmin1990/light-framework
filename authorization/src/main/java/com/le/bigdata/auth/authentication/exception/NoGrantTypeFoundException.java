package com.le.bigdata.auth.authentication.exception;

import javax.servlet.ServletException;

/**
 * Created by benjamin on 9/12/14.
 */
public class NoGrantTypeFoundException extends ServletException {

    public NoGrantTypeFoundException() {
        super();
    }

    public NoGrantTypeFoundException(String message) {
        super(message);
    }

    public NoGrantTypeFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
