package com.j13.ryze.cache;

import com.j13.poppy.JedisManager;
import com.j13.poppy.TokenCacheInterface;
import com.j13.poppy.config.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Random;

/**
 * <p>
 * redis存储格式一共两种
 * key=UserId2Token是为了帮助通过userId查出对应的token，在登陆的时候需要去掉之前所有相关的token的缓存，需要反查出这个缓存，避免有两个可以操作的token
 * key=token是为了判断用户客户端上报的token是否合法，如果合法的话可以查询出对应的userId，如果缓存失效需要重新登陆
 * token例子：v10:token:123848739283=1344
 * userId2Token例子：v10:userId2Token:1344=123848739283
 * <p>
 */
@Service
public class TokenCache implements TokenCacheInterface {

    private static int TOKEN_EXPIRE_S = 0;
    private Random random = new Random();

    @Autowired
    PropertiesConfiguration propertiesConfiguration;
    @Autowired
    JedisManager jedisManager;

    @PostConstruct
    public void init() {
        TOKEN_EXPIRE_S = propertiesConfiguration.getIntValue("token.expire.s");
    }


    public String getUserId2Token(int userId) {
        String t = jedisManager.get(PREFIX + "userId2Token:" + userId);
        return t;
    }


    public void setUserId2Token(int userId, String t) {
        jedisManager.set(PREFIX + "userId2Token:" + userId, t);
    }

    public void deleteUserId2Token(int userId) {
        jedisManager.delete(PREFIX + "userId2Token:" + userId);
    }


    public String genToken() {
        int i = this.random.nextInt(10000);
        return System.currentTimeMillis() + (long) i + "";
    }

    public int getToken2UserId(String token) {
        String value = this.jedisManager.get(PREFIX + "token:" + token);
        return value == null ? 0 : new Integer(value);
    }

    public void setToken2UserId(String token, int userId) {
        this.jedisManager.set(PREFIX + "token:" + token, userId + "", this.TOKEN_EXPIRE_S);
    }

    public void deleteToken2UserId(String token) {
        this.jedisManager.delete(PREFIX + "token:" + token);
    }

}