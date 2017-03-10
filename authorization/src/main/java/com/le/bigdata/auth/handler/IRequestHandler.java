package com.le.bigdata.auth.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by benjamin on 9/11/14.
 */
public interface IRequestHandler {
    /**
     * handle
     *
     * @param request
     * @param response
     */
    boolean handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
