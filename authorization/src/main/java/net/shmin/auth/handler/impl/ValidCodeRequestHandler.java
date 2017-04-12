package net.shmin.auth.handler.impl;

import net.shmin.auth.handler.IRequestHandler;
import net.shmin.core.util.CodecUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import static net.shmin.core.Constant.VERIFY_CODE;

/**
 * 验证签名值
 * Created by benjamin on 9/11/14.
 */
@Component
public class ValidCodeRequestHandler implements IRequestHandler {
    @Override
    public boolean handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String sortedParam = sortedParam(request);
        // 对比签名值
        //
        String clientHashValue = request.getHeader(VERIFY_CODE);
        // 无参
        if (sortedParam == null || sortedParam.isEmpty()) {
            return true;
        } else {
            String hashValue = CodecUtil.md5hex(sortedParam);
            return hashValue.equals(clientHashValue);
        }
    }

    /**
     * 将请求参数按照英文首字母排序
     * example: a=b&ab=c&d=e
     * 客户端也要按照此排序规则对request做签名值,(参照sina的oauth实现(少许修改)，http://open.weibo.com/wiki/Oauth)
     * 然后对比md5值
     *
     * @param request
     * @return
     */
    private String sortedParam(HttpServletRequest request) throws IOException {
        String contentType = request.getContentType();
        // 目前只验证表单类型的数据
        if (contentType.contains("application/www-x-form-urlencoded")){
            List<String> params = new ArrayList<String>();
            Enumeration<String> enumeration = request.getParameterNames();
            while (enumeration.hasMoreElements()) {
                params.add(enumeration.nextElement());
            }
            Collections.sort(params, String.CASE_INSENSITIVE_ORDER);
            StringBuilder stringBuilder = new StringBuilder();
            for (String paramName : params) {
                stringBuilder.append(paramName);
                stringBuilder.append("=");
                stringBuilder.append(request.getParameter(paramName));
                stringBuilder.append("&");
            }
            if (stringBuilder.toString().isEmpty()) {
                return null;
            }
            return stringBuilder.toString().substring(0, stringBuilder.length() - 1);
        }
        return null;
    }
}
