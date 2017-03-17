package com.le.bigdata.auth.authentication.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.le.bigdata.auth.authentication.PasswordValidator;
import com.le.bigdata.core.dto.CommonResponseDTO;
import com.le.bigdata.core.util.CodecUtil;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by benjamin on 2016/12/12.
 * 乐视网 oss 用户名登录
 */
@Component("passwordValidator")
public class OSSPasswordValidator implements PasswordValidator {

    private static final String passEncyptUrl = "http://sso.leshiren.cn:20008/transcode.php";
    private static final String loginUrl = "http://sso.leshiren.cn:20008/check_user.php";
    private static final String site = "DMP";
    private static final String key = "vfi1wsx3efv4thn";
    private static final String type = "ENCODE";
    private static final String USER_INFO_URL = "http://sso.leshiren.cn:20008/user.php";

    @Override
    public CommonResponseDTO login(HttpServletRequest request) throws Exception {
        String username = null;
        String password = null;
        try {
            username = URLDecoder.decode(request.getParameter("username"), "UTF-8");
            password = URLDecoder.decode(request.getParameter("password"), "UTF-8");
            if (username == null || username.isEmpty()) {
                throw new Exception("用户名不能为空");
            }
            if (password == null || password.isEmpty()) {
                throw new Exception("密码不能为空");
            }
            JSONObject jsonResult = ossLogin(username, password);
            Boolean responseStatus = jsonResult.getJSONObject("respond").getBoolean("status");
            return new CommonResponseDTO(responseStatus ? 200 : 10000);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new CommonResponseDTO(10000);

    }

    private JSONObject ossLogin(String username, String password) throws IOException {
        long time = new Date().getTime() / 1000;
        username = URLEncoder.encode(username, "UTF-8");
        password = URLEncoder.encode(password, "UTF-8");

        /**
         * 1:得到密码sign 2:对密码加密 3:得到登录sign 4:请求用户是否存在
         */
        String passSign = getPassSign(password, time);
        String encyptPass = getEncyptPass(password, passSign, time);
        // {"respond":{"status":1,"code":"0","msg":"\u8bf7\u6c42\u6210\u529f"},"objects":"085djbkWZusmWHPUtruCrX7\/mc9fjhG8TcJFt1+FlwCzpKWpLRM"}
        encyptPass = getPassObject(encyptPass);

        String loginSign = getLoginSign(username, encyptPass, time);
        String result = goForLogin(username, encyptPass, loginSign, time);
//        CommonResponseDTO responseDTO = new CommonResponseDTO();
        JSONObject jsonResult = JSON.parseObject(result);
        return jsonResult;
    }

    private String getPassObject(String encyptPass) {
        JSONObject j = JSONObject.parseObject(encyptPass);
        return j.getString("objects");
    }

    private String goForLogin(String username, String encptPass,
                              String loginSign, long time) throws IOException {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(loginUrl);
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("password", encptPass));
            list.add(new BasicNameValuePair("site", site));
            list.add(new BasicNameValuePair("time", time + ""));
            list.add(new BasicNameValuePair("username", username));
            list.add(new BasicNameValuePair("sign", loginSign));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "utf-8");
            return result;
        } finally {
            if (httpClient != null)
                httpClient.close();
            if (response != null)
                response.close();
        }
    }

    private String getLoginSign(String username, String password, long time) throws UnsupportedEncodingException {
        String str = "password=" + password + "&site=" + site + "&time=" + time
                + "&username=" + username + key;
        String loginSign = CodecUtil.md5hex(str);
        return loginSign.toLowerCase();
    }

    private String getEncyptPass(String password, String passSign, long time) throws IOException {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(passEncyptUrl);
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("site", site));
            list.add(new BasicNameValuePair("time", time + ""));
            list.add(new BasicNameValuePair("type", type));
            list.add(new BasicNameValuePair("v", password));
            list.add(new BasicNameValuePair("sign", passSign));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            response = httpClient.execute(httpPost);
//            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "utf-8");
            return result;
        } finally {
            if (httpClient != null)
                httpClient.close();
            if (response != null)
                response.close();
        }

    }

    private String getPassSign(String password, long time) throws UnsupportedEncodingException {
        String passStr = "site=" + site + "&time=" + time + "&type=" + type
                + "&v=" + password + key;
        return CodecUtil.md5hex(passStr).toLowerCase();
    }
}
