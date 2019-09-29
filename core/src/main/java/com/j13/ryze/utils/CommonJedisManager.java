package com.j13.ryze.utils;

import com.alibaba.fastjson.JSON;
import com.j13.poppy.JedisManager;
import com.j13.poppy.config.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Lazy
public class CommonJedisManager {
    private static String PREFIX = "v10:";
    private static int OBJECT_EXPIRE_S = 0;

    @Autowired
    JedisManager jedisManager;

    @Autowired
    PropertiesConfiguration propertiesConfiguration;

    @PostConstruct
    public void init(){
        OBJECT_EXPIRE_S = propertiesConfiguration.getIntValue("object.expire.s");
    }

    public <T> T get(String catalog, Object key, Class<T> clazz) {
        String value = jedisManager.get(PREFIX + catalog + ":" + key.toString());
        if (value == null) {
            return null;
        }
        return JSON.parseObject(value, clazz);
    }

    public <T> List<T>  getArray(String catalog, Object key, Class<T> clazz) {
        String value = jedisManager.get(PREFIX + catalog + ":" + key.toString());
        if (value == null) {
            return null;
        }
        return JSON.parseArray(value, clazz);
    }

    public void set(String catalog, Object key, Object value) {
        jedisManager.set(PREFIX + catalog + ":" + key, JSON.toJSONString(value), OBJECT_EXPIRE_S);
    }

    public void setAccessToken(String token,int expireS){
        jedisManager.set(PREFIX +"accessToken",token, expireS);
    }

    public String getAccessToken(){
        return jedisManager.get(PREFIX+"accessToken");
    }

}
