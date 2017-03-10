package com.le.bigdata.auth.dto;

import java.io.Serializable;

/**
 * Created by benjamin on 16/3/31.
 */
public class CommonResponseDTO implements Serializable {

    private boolean success;
    private Object data;
    private String errorMsg;

    public CommonResponseDTO() {

    }

    public CommonResponseDTO(boolean success, Object data, String errorMsg) {
        this.success = success;
        this.data = data;
        this.errorMsg = errorMsg;
    }

    public CommonResponseDTO(boolean success) {
        this(success, null, null);
    }

    public CommonResponseDTO(boolean success, Object data) {
        this(success, data, null);
    }

    public CommonResponseDTO(boolean success, String data) {
        this(success, null, data);
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
}
