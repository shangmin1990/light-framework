package com.le.bigdata.core;

/**
 * Created by benjamin on 16/3/31.
 */
public interface Constant {
    String REDIRECT_URI = "redirect_uri";
    String CLIENT_ID = "client_id";
    String STATE = "state";
    // 验证请求是否被篡改
    String VERIFY_CODE = "Verify-Code";
    // 登录的唯一标示
    String ACCESS_TOKEN = "access_token";

    String RESPONSE_TYPE = "response_type";
    String USER_COOKIE_NAME = "user-cookie-name";
    String DEFAULT_USER_COOKIE_NAME = "username";
    String GRANT_TYPE = "grant_type";
    String USERNAME = "username";
    String CHARSET_UTF8 = "UTF-8";

    String OK = "OK";

//  String TOKEN_SEPARATOR = "~~";
}
