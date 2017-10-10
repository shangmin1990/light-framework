package net.shmin.auth.token.impl;

import com.alibaba.fastjson.JSONObject;
import net.shmin.auth.AuthContext;
import net.shmin.auth.token.IAuthTokenGenerator;
import net.shmin.auth.token.Token;
import net.shmin.auth.token.TokenType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by benjamin on 2017/3/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class SimpleTokenGeneratorImplTest {

    @Autowired
    private IAuthTokenGenerator authTokenGenerator;

    @Autowired
    private AuthContext authContext;

    private Token token;

    @Before
    public void before(){
        token = authTokenGenerator.generateAccessToken(true);
    }


    /**
     * 测试设置token有效期是否生效
     * Expect: 如果设置了使用新的值, 如果没有设置, 则采用默认值
     * @throws Exception
     */
    @Test
    public void testPropertiesOverride() throws Exception {
        Token token = authTokenGenerator.generateAccessToken(true);
        Token code = authTokenGenerator.generateAuthorizationCode();
        long expires = token.getExpires();
        Assert.assertTrue(expires == 60 * 1000L);
        Assert.assertTrue(code.getExpires() == 10 * 60 * 1000L);
        Assert.assertTrue(token.getRefreshToken().getExpires() == 30 * 24 * 3600 * 1000L);
        Assert.assertTrue(authContext.getAccessTokenRedisDatabase() == 1);
        Assert.assertTrue(authContext.getRefreshTokenRedisDatabase() == 3);
        Assert.assertTrue(authContext.getAuthorizationCodeRedisDatabase() == 2);
        Assert.assertTrue(authContext.getTokenKeyPrefix().equals(""));
        Assert.assertTrue(authContext.getAccessTokenKeySuffix().equals("_ACCESS_TOKEN"));
    }


//    @Test
    public void testSaveToken(){

        authContext.getAuthTokenProvider().saveToken(token);

        boolean result = authContext.getAuthTokenProvider().checkToken(token.getValue(), token.getTokenType());

        Assert.assertTrue(result);

        boolean result1 = authContext.getAuthTokenProvider().checkToken(token.getValue(), TokenType.refreshToken);

        Assert.assertTrue(!result1);

        boolean result2 = authContext.getAuthTokenProvider().checkToken(token.getValue(), TokenType.authorizationCode);

        Assert.assertTrue(!result2);
    }

//    @Test
    public void testAttribute(){

        Token token = authTokenGenerator.generateAccessToken(true);

        authContext.getAuthTokenProvider().saveToken(token);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "abc");
        authContext.setAttribute(token.getValue(), "user", jsonObject);

        JSONObject result = authContext.getAttribute(token.getValue(), "user", JSONObject.class);
        Assert.assertTrue(result.equals(jsonObject));

        authContext.removeAttribute(token.getValue(), "user");

        JSONObject result1 = authContext.getAttribute(token.getValue(), "user", JSONObject.class);
        Assert.assertTrue(result1 == null);
    }
}
