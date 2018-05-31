package net.shmin.auth.util;

import net.shmin.auth.authentication.TokenResponseDTO;
import net.shmin.auth.token.Token;
import net.shmin.core.Constant;
import net.shmin.core.dto.CommonResponseDTO;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by benjamin on 9/3/14.
 */
public class WebUtil implements Constant {

    private static final String AJAX_HEADER = "X-Requested-With";

    private static final String XMLHTTPREQUEST = "xmlhttprequest";

    private static final String DEFAULT_NO_ACCESS = "no access";

    private WebUtil() {

    }

    public static void reply(HttpServletRequest request, HttpServletResponse response, int code, String responseText) throws IOException {
        String encoding = request.getCharacterEncoding();
        response.setCharacterEncoding(encoding);
        response.setContentType("text/plain;charset=UTF-8");
        response.setStatus(code);
        PrintWriter out = response.getWriter();
        out.println(responseText);
        out.flush();
        out.close();
    }

    public static void replyNoAccess(HttpServletRequest request, HttpServletResponse response, String responseText) throws IOException {
        reply(request, response, 401, responseText);
    }

    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestType = request.getHeader(AJAX_HEADER);
        return requestType != null && requestType.toLowerCase().equals(XMLHTTPREQUEST);
    }

    public static void response(HttpServletRequest request, HttpServletResponse httpServletResponse, Token token, String access_token_name, Object obj) {
//        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //String encoding = httpServletRequest.getCharacterEncoding();
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setCharacterEncoding(CHARSET_UTF8);
        PrintWriter out = null;
        try {
            out = httpServletResponse.getWriter();
            TokenResponseDTO tokenResponseDTO = new TokenResponseDTO();
            tokenResponseDTO.setAccess_token(token.getValue());
            tokenResponseDTO.setExpires_in(token.getExpires());
            tokenResponseDTO.setAccessTokenCookieName(access_token_name);
//            tokenResponseDTO.setUsernameCookieName(username_cookie_name);
            tokenResponseDTO.setData(obj);
            CommonResponseDTO common = CommonResponseDTO.success(tokenResponseDTO);
            out.println(common.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }
}
