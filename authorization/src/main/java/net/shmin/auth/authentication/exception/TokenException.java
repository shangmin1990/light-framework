package net.shmin.auth.authentication.exception;

import net.shmin.core.exception.BusinessServiceException;

/**
 * @Author: benjamin
 * @Date: Create in  2017/9/29 上午11:46
 * @Description:
 */
public class TokenException extends BusinessServiceException {

    public TokenException(int code) {
        super(code);
    }

    public TokenException(int code, Exception e) {
        super(code, e);
    }

    public TokenException(int code, Throwable throwable) {
        super(code, throwable);
    }

    public TokenException(int code, String errorMsg, Throwable throwable) {
        super(code, errorMsg, throwable);
    }

    public TokenException(int code, String errorMsg) {
        super(code, errorMsg);
    }
}
