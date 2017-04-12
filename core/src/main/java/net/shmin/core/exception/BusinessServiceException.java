package net.shmin.core.exception;

/**
 * Created by benjamin on 2017/3/13.
 */
public class BusinessServiceException extends RuntimeException {

    protected int code;

    public BusinessServiceException(int code){
        this.code = code;
    }

    public BusinessServiceException(int code, Exception e){
        super(e);
        this.code = code;
    }

    public BusinessServiceException(int code, Throwable throwable){
        super(throwable);
        this.code = code;
    }

    public BusinessServiceException(int code, String errorMsg, Throwable throwable){
        super(errorMsg, throwable);
        this.code = code;
    }

    public BusinessServiceException(int code, String errorMsg){
        super(errorMsg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
