package com.le.bigdata.core.dto;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by benjamin on 16/3/31.
 */
public class CommonResponseDTO implements Serializable {

//    private boolean success;
    private int code;
    private Object data;
    private String errorMsg;

    public CommonResponseDTO() {

    }

    public CommonResponseDTO(int code, Object data, String errorMsg) {
//        this.success = success;
        this.data = data;
        this.errorMsg = errorMsg;
        this.code = code;
    }

    public CommonResponseDTO(int code) {
        this(code, null, null);
    }

    public CommonResponseDTO(int code, Object data) {
        this(code, data, null);
    }

    public CommonResponseDTO(int code, String errorMsg) {
        this(code, null, errorMsg);
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

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
