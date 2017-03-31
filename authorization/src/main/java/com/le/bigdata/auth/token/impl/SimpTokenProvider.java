package com.le.bigdata.auth.token.impl;

import com.le.bigdata.auth.token.Token;
import com.le.bigdata.auth.token.TokenType;
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
@Deprecated
public class SimpTokenProvider extends AbstractTokenProvider {

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

        if(key == null || key.isEmpty() || tokenValue != null){
            return false;
        }

        switch (tokenValue.getTokenType()){
            case accessToken:
                return tokenValue.equals(accessTokens.get(key));
            case refreshToken:
                return tokenValue.equals(refreshTokens.get(key));
            case authorizationCode:
                return tokenValue.equals(codes.get(key));
        }
        return false;
    }

    @Override
    public void saveToken(String key, Token token) {

        switch (token.getTokenType()){
            case accessToken:
                accessTokens.putIfAbsent(key, token);
                break;
            case refreshToken:
                refreshTokens.putIfAbsent(key, token);
                break;
            case authorizationCode:
                codes.putIfAbsent(key, token);
                break;
        }
        if(token.getRefreshToken() != null){
            saveToken(key, token.getRefreshToken());
        }
    }

    @Override
    public String getToken(String key, TokenType tokenType) {
        Token token = null;
        switch (tokenType){
            case accessToken:
                token = accessTokens.get(key);
                break;
            case refreshToken:
                token = refreshTokens.get(key);
                break;
            case authorizationCode:
                token = codes.get(key);
                break;
        }
        if(token == null){
            return null;
        }
        return token.getValue();
    }

    @Override
    public void deleteToken(String key, TokenType tokenType) {
        switch (tokenType){
            case accessToken:
                accessTokens.remove(key);
                break;
            case refreshToken:
                refreshTokens.remove(key);
                break;
            case authorizationCode:
                codes.remove(key);
                break;
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
