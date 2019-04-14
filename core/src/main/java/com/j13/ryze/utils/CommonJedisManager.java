package com.j13.ryze.utils;

import com.alibaba.fastjson.JSON;
import com.j13.poppy.JedisManager;
import com.j13.poppy.config.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class CommonJedisManager {
    private static String PREFIX = "v10:";
    private static int OBJECT_EXPIRE_S = PropertiesConfiguration.getInstance().getIntValue("object.expire.s");

    @Autowired
    JedisManager jedisManager;

    public <T> T get(String catalog, Object key, Class<T> clazz) {
        String value = jedisManager.get(PREFIX + catalog + ":" + key.toString());
        if (value == null) {
            return null;
        }
        return JSON.parseObject(value, clazz);
    }


    public void set(String catalog, Object key, Object value) {
        jedisManager.set(PREFIX + catalog + ":" + key, JSON.toJSONString(value), OBJECT_EXPIRE_S);
    }

}
