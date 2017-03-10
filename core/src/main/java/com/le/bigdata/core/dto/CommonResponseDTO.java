package com.le.bigdata.core.dto;

import java.io.Serializable;

/**
 * Created by benjamin on 16/3/31.
 */
public class CommonResponseDTO implements Serializable {

    private boolean success;
    private int code;
    private Object data;
    private String errorMsg;

    public CommonResponseDTO() {

    }

    public CommonResponseDTO(boolean success, int code, Object data, String errorMsg) {
        this.success = success;
        this.data = data;
        this.errorMsg = errorMsg;
        this.code = code;
    }

    public CommonResponseDTO(boolean success) {
        this(success, 200, null, null);
    }

    public CommonResponseDTO(boolean success, Object data) {
        this(success, 200, data, null);
    }

    public CommonResponseDTO(boolean success, int code, String errorMsg) {
        this(success, code, null, errorMsg);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
