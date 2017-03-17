package com.le.bigdata.common.controller;

import com.le.bigdata.core.Constant;
import com.le.bigdata.core.dto.CommonResponseDTO;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by benjamin on 2017/2/21.
 */
@RestController
@RequestMapping("forward")
public class HttpForwardController implements Constant {

    @RequestMapping(value = "get", method = RequestMethod.GET)
    public CommonResponseDTO forward(@RequestParam String target) {
        CloseableHttpClient closeableHttpClient;
        CloseableHttpResponse response;
        closeableHttpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(target);
//        httpGet.setHeader(new BasicHeader("Cookie", "letv_user_name=shangmin;"));
        try {
            response = closeableHttpClient.execute(httpGet);
            String res = EntityUtils.toString(response.getEntity(), CHARSET_UTF8);
            return new CommonResponseDTO(200, res, null);
        } catch (IOException e) {
            e.printStackTrace();
            return new CommonResponseDTO(500, null, e.getMessage());
        }
    }

    @RequestMapping(value = "post", method = RequestMethod.POST)
    public CommonResponseDTO forwardPost(@RequestParam String target,
                                         @RequestBody Map<String, String> params) {
        CloseableHttpClient closeableHttpClient;
        CloseableHttpResponse response;
        closeableHttpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(target);
//        post.setHeader(new BasicHeader("Cookie", "letv_user_name=shangmin;"));
        List<NameValuePair> list = new ArrayList<>();
        if (params != null) {
            Set<String> keys = params.keySet();
            Iterator<String> keyIterator = keys.iterator();
            while (keyIterator.hasNext()) {
                String key = keyIterator.next();
                list.add(new BasicNameValuePair(key, params.get(key)));
            }
            try {
                post.setEntity(new UrlEncodedFormEntity(list, CHARSET_UTF8));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return new CommonResponseDTO(500, null, e.getMessage());
            }
        }
        try {
            response = closeableHttpClient.execute(post);
            String res = EntityUtils.toString(response.getEntity(), CHARSET_UTF8);
            return new CommonResponseDTO(200, res, null);
        } catch (IOException e) {
            e.printStackTrace();
            return new CommonResponseDTO(500, null, e.getMessage());
        }
    }
}
