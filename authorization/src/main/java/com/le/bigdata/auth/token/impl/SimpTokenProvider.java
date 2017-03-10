package com.le.bigdata.auth.token.impl;

import com.le.bigdata.auth.token.IAuthTokenProvider;
import com.le.bigdata.auth.token.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton
 * Created by benjamin on 9/4/14.
 */
public class SimpTokenProvider implements IAuthTokenProvider {

    private Logger logger = LoggerFactory.getLogger(SimpTokenProvider.class);

    /**
     * accessToken
     */
    private static ConcurrentHashMap<String, Token> accessTokens = new ConcurrentHashMap<String, Token>();
    /**
     * token
     */
    private static ConcurrentHashMap<String, Token> codes = new ConcurrentHashMap<String, Token>();
    /**
     * refreshToken
     */
    private static ConcurrentHashMap<String, Token> refreshTokens = new ConcurrentHashMap<String, Token>();

    private SimpTokenProvider() {
        initializer();
    }

    private static SimpTokenProvider simpTokenProvider = new SimpTokenProvider();

    public static SimpTokenProvider getInstance() {
        return simpTokenProvider;
    }

    //  @Override
    private void initializer() {
        TokenCheckTask.checkTokenExpires();
    }

    @Override
    public boolean checkToken(String key, Token tokenValue) {

        if (tokenValue.isAuthorizationCode()) {
            return codes.contains(key);
        } else if (tokenValue.isRefresh()) {
            return refreshTokens.contains(key);
        } else {
            return accessTokens.contains(key);
        }
    }

    @Override
    public void saveToken(String key, Token token) {
        if (key != null) {
            if (token.isAuthorizationCode()) {
                codes.put(key, token);
            } else if (token.isRefresh()) {
                refreshTokens.put(key, token);
            } else {
                accessTokens.put(key, token);
            }
        }
    }

    @Override
    public String getAccessToken(String key) {
        if (key != null) {
            Token token = accessTokens.get(key);
            return token.getValue();
        }
        return null;
    }

    @Override
    public String getAuthorizationCode(String key) {
        return key == null ? null : codes.get(key).getValue();
    }

    @Override
    public String refreshToken(Token token) {

        return null;
    }

    @Override
    public void deleteToken(String key) {
        if (accessTokens.containsKey(key)) {
            accessTokens.remove(key);
        }
        if (refreshTokens.contains(key)) {
            refreshTokens.remove(key);
        }
        if (accessTokens.contains(key)) {
            accessTokens.remove(key);
        }
    }

    @Override
    public void destroy() {
        TokenCheckTask.timerTask.cancel();
        accessTokens.clear();
        codes.clear();
        refreshTokens.clear();
    }

    static class TokenCheckTask {

        private static Timer timer = new Timer();
        private static TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Collection<Token> accessTokenCollection = accessTokens.values();
                Collection<Token> authorizationCodes = codes.values();
                if (accessTokenCollection.size() > 0) {
                    Iterator<Token> tokenIterator = accessTokenCollection.iterator();
                    while (tokenIterator.hasNext()) {
                        Token token = tokenIterator.next();
                        //token 已经过期了
                        if (System.currentTimeMillis() - token.getGeneratorTime() > token.getExpires()) {
                            tokenIterator.remove();
                            accessTokenCollection.remove(token);
                            System.out.println("remove Token value " + token.getValue());
                            System.out.println("还剩余" + accessTokenCollection.size() + "个Token");
                        }
                    }
                }

                if (authorizationCodes.size() > 0) {
                    Iterator<Token> tokenIterator = authorizationCodes.iterator();
                    while (tokenIterator.hasNext()) {
                        Token token = tokenIterator.next();
                        //token 已经过期了
                        if (System.currentTimeMillis() - token.getGeneratorTime() > token.getExpires()) {
                            tokenIterator.remove();
                            accessTokenCollection.remove(token);
                            System.out.println("remove Token value " + token.getValue());
                            System.out.println("还剩余" + authorizationCodes.size() + "个AccessToken");
                        }
                    }
                }
            }
        };

        static void checkTokenExpires() {
            timer.scheduleAtFixedRate(timerTask, 0, 100);
        }
    }
}
