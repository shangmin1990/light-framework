package net.shmin.core.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by benjamin on 2017/3/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-core.xml")
public class JedisPoolUtilTest {

    @Autowired
    private JedisPoolUtil jedisPoolUtil;

    /**
     * 检查属性设置.
     * @throws InterruptedException
     */
    @Test
    public void test() throws InterruptedException {
        Assert.assertTrue("12.42.17.42".equals(jedisPoolUtil.getHost()));
        Assert.assertTrue("123456".equals(jedisPoolUtil.getPassword()));
        Assert.assertTrue(jedisPoolUtil.getMaxIdle() == 10);
    }
}
