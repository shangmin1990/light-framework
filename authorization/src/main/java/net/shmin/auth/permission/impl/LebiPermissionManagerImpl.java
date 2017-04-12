package net.shmin.auth.permission.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.shmin.auth.permission.IPermissionValidator;
import net.shmin.auth.permission.model.ACLEnum;
import net.shmin.core.util.SpringContextUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.*;

import static net.shmin.core.Constant.CHARSET_UTF8;

/**
 * Created by benjamin on 2017/1/4.
 */
public class LebiPermissionManagerImpl implements IPermissionValidator<String, String> {

    private String lebiPermissionServer;

    public LebiPermissionManagerImpl() {
        String profile = SpringContextUtils.getApplicationContext().getEnvironment().getDefaultProfiles()[0];
        String prefix = "lebi";
        String suffix = ".properties";
        String fileName = prefix + suffix;
        if (profile != null && !profile.isEmpty()) {
            fileName = prefix + "." + profile + suffix;
        }
        Resource resource = SpringContextUtils.getApplicationContext().getResource("classpath:" + fileName);
        Properties properties = new Properties();
        try {
            properties.load(resource.getInputStream());
            lebiPermissionServer = properties.getProperty("lebi.permission.server");
        } catch (IOException e) {
            e.printStackTrace();
            if (profile != null && !profile.isEmpty()) {
                if ("release".equals(profile)) {
                    lebiPermissionServer = "http://lebi.letv.cn";
                } else {
                    lebiPermissionServer = "http://test.lebi.letv.cn";
                }
            }
        }

    }

    @Override
    public boolean hasPermission(String userId, String[] resourceIds, ACLEnum[] needed) throws IOException {
//        String[] array = new String[1];
//        array[0] = resourceId;
        if (resourceIds == null || resourceIds.length == 0) {
            return true;
        }

        StringBuilder stringBuilder = new StringBuilder("[");

        for (String resourceId : resourceIds) {
            stringBuilder.append("\"" + resourceId + "\"");
            stringBuilder.append(",");
        }
        String resourceListString = stringBuilder.substring(0, stringBuilder.length() - 1);

        resourceListString += "]";

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(lebiPermissionServer + "/api/v1/auth");
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("userName", getUserNameById(userId)));
            nameValuePairs.add(new BasicNameValuePair("resourceList", resourceListString));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, CHARSET_UTF8));
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity(), CHARSET_UTF8);
                JSONObject jsonObject = JSON.parseObject(result);
                if (jsonObject.getBoolean("success")) {
                    JSONArray permissions = jsonObject.getJSONArray("data");
                    if (permissions.size() > 0) {
                        JSONArray permission = permissions.getJSONArray(0);
                        return checkPermission(permission, needed);
                    }
                }
            }
        } finally {
            if (httpClient != null)
                httpClient.close();
            if (response != null)
                response.close();
        }
        return false;
    }

    private boolean checkPermission(JSONArray permission, ACLEnum[] needed) {
        if (permission == null || permission.size() == 0) {
            return false;
        }
//        Set<ACLEnum> permissionSet = new HashSet<>();
        Set<String> permissionSet = new HashSet<>();
        for (int i = 0; i < permission.size(); i++) {
            permissionSet.add(permission.getString(i));
        }
        if (permissionSet.contains("ALL")) {
            return true;
        }
        for (ACLEnum aclEnum : needed) {
            switch (aclEnum) {
                case RETRIEVE:
                    if (!permissionSet.contains("PERMIT")) {
                        return false;
                    }
                    break;
                case UPDATE:
                    if (!permissionSet.contains("WRITE")) {
                        return false;
                    }
                    break;
                case CREATE:
                    if (!permissionSet.contains("CREATE")) {
                        return false;
                    }
                    break;
                case DELETE:
                    if (!permissionSet.contains("DELETE")) {
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

    private String getUserNameById(String userId) {
        return userId;
    }
}
