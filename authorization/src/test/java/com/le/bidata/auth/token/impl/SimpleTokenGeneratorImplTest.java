package com.le.bidata.auth.token.impl;

import com.le.bigdata.auth.token.IAuthTokenGenerator;
import com.le.bigdata.auth.token.IAuthTokenProvider;
import com.le.bigdata.auth.token.Token;
import com.le.bigdata.auth.token.impl.RedisTokenProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by benjamin on 2017/3/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class SimpleTokenGeneratorImplTest {

    @Autowired
    private IAuthTokenGenerator authTokenGenerator;

    @Resource(name = "redisTokenProvider")
    private IAuthTokenProvider tokenProvider;

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

//        tokenProvider.saveToken("a", token);

//        boolean result = tokenProvider.checkToken("a", token);

        RedisTokenProvider redisTokenProvider = (RedisTokenProvider) tokenProvider;
        Assert.assertTrue(redisTokenProvider.getACCESS_TOKEN_STORAGE() == 1);
        Assert.assertTrue(redisTokenProvider.getREFRESH_TOKEN_STORAGE() == 3);
        Assert.assertTrue(redisTokenProvider.getAUTHORIZATION_CODE_STORAGE() == 2);
        Assert.assertTrue(redisTokenProvider.getTOKEN_PREFIX().equals(""));
        Assert.assertTrue(redisTokenProvider.getACCESS_TOKEN_SUFFIX().equals("_ACCESS_TOKEN"));
//        Assert.assertTrue(result);
    }
}
