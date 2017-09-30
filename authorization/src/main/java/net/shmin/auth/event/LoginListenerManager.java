package net.shmin.auth.event;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @Author: benjamin
 * @Date: Create in  2017/9/29 下午3:49
 * @Description:
 */
@Component
public class LoginListenerManager implements ApplicationContextAware {

    private Collection<LoginListener> synchronizedCollection;

    private ApplicationContext applicationContext;

    @PostConstruct
    public void init(){
        List<LoginListener> listeners = new ArrayList<>();
        synchronizedCollection = Collections.synchronizedCollection(listeners);
        Map<String, LoginListener> beans = applicationContext.getBeansOfType(LoginListener.class);
        if (applicationContext.getParent() != null){
            ApplicationContext context = applicationContext.getParent();
            Map<String, LoginListener> beansParent = context.getBeansOfType(LoginListener.class);
            beans.putAll(beansParent);
        }
        Set<String> keySet = beans.keySet();
        for (String key: keySet){
            addListener(beans.get(key));
        }
    }


    /**
     * 添加事件
     *
     * @param listener
     *
     */
    public void addListener(LoginListener listener) {
        synchronizedCollection.add(listener);
    }

    /**
     * 移除事件
     *
     * @param listener
     *
     */
    public void removeListener(LoginListener listener) {
        synchronizedCollection.remove(listener);
    }

    /**
     * 触发事件
     */
    public void fireEvent(LoginEvent eventObject) {
        notifyListeners(eventObject);
    }

    /**
     * 通知所有的DoorListener
     */
    private void notifyListeners(LoginEvent event) {
        Iterator iter = synchronizedCollection.iterator();
        while (iter.hasNext()) {
            LoginListener listener = (LoginListener) iter.next();
            listener.loginHandler(event);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
