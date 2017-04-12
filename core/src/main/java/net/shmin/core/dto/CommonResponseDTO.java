package net.shmin.core.dto;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by benjamin on 16/3/31.
 */
public class CommonResponseDTO implements Serializable {

    private boolean success;
    private Object data;
    private Error error;

    public static CommonResponseDTO success(Object data){
        return new CommonResponseDTO(true, data, null);
    }

    public static CommonResponseDTO success(){
        return new CommonResponseDTO(true, null, null);
    }

    public static CommonResponseDTO error(int errorCode, String message){
        Error error = new Error(errorCode, message);
        return new CommonResponseDTO(false, null, error);
    }

    private CommonResponseDTO(boolean result, Object data, Error error) {
        this.success = result;
        this.data = data;
        this.error = error;
    }

    public Object getData() {
        return data;
    }


    public Error getError() {
        return error;
    }

    public boolean isSuccess() {
        return success;
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static CommonResponseDTO instance(Boolean responseStatus, Object data, int errorCode, String errorMsg) {
        Error error = new Error(errorCode, errorMsg);
        return new CommonResponseDTO(responseStatus, data, error);
    }

    public static CommonResponseDTO failure() {
        return new CommonResponseDTO(false, null, null);
    }

    public static CommonResponseDTO status(Boolean responseStatus) {
        return new CommonResponseDTO(responseStatus, null, null);
    }


    static class Error {

        private int error_code;

//        private Throwable throwable;

        private String errorMsg;

        private Error(int code){
            error_code = code;
        }

        private Error(int code, String message){
            error_code = code;
//            throwable = cause;
            this.errorMsg = message;
        }

        public int getError_code() {
            return error_code;
        }

        public void setError_code(int error_code) {
            this.error_code = error_code;
        }

//        public Throwable getThrowable() {
//            return throwable;
//        }
//
//        public void setThrowable(Throwable throwable) {
//            this.throwable = throwable;
//        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }
    }
}

