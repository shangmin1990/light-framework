package net.shmin.auth.token;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author: benjamin
 * @Date: Create in  2017/9/27 下午3:55
 * @Description:
 */
@Component("redisTokenProvider")
public class TokenProviderFactory implements FactoryBean<IAuthTokenProvider>, ApplicationContextAware {

    @Value("${token.provider.beanName}")
    private String tokenProviderBeanName;

    private final String DEFAULT_PROVIDER_BEAN_NAME = "redisTokenProviderImpl";

    private ApplicationContext applicationContext;

    public String getTokenProviderBeanName() {
        return tokenProviderBeanName;
    }

    public void setTokenProviderBeanName(String tokenProviderBeanName) {
        this.tokenProviderBeanName = tokenProviderBeanName;
    }

    @Override
    public IAuthTokenProvider getObject() throws Exception {
        if (tokenProviderBeanName == null || tokenProviderBeanName.isEmpty() || "${token.provider.beanName}".equals(tokenProviderBeanName)){
            tokenProviderBeanName = DEFAULT_PROVIDER_BEAN_NAME;
        }
        return applicationContext.getBean(tokenProviderBeanName, IAuthTokenProvider.class);
    }

    @Override
    public Class<?> getObjectType() {
        return IAuthTokenProvider.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
