package net.shmin.auth;

/**
 * @Author: benjamin
 * @Date: Create in  2018/6/7 上午11:16
 * @Description:
 */
public interface AuthErrorCode extends TokenError{

    /**
     * 请求校验码不正确
     */
    int INVALID_SIGNATURE = 4002;

    /**
     * 权限不足
     */
    int PRIVILEGE_ERROR = 4003;

    /**
     * 服务器内部错误
     */
    int SERVICE_EXCEPTION = 5000;
}
