package com.j13.ryze.utils;

import com.alibaba.fastjson.JSON;
import com.j13.poppy.JedisManager;
import com.j13.poppy.config.PropertiesConfiguration;

public class CommonJedisManager extends JedisManager {
    private static String PREFIX = "v10:";
    private static int OBJECT_EXPIRE_S = PropertiesConfiguration.getInstance().getIntValue("object.expire.s");


    public <T> T get(String catalog, Object key, Class<T> clazz) {
        String value = get(PREFIX + catalog + ":" + key.toString());
        if (value == null) {
            return null;
        }
        return JSON.parseObject(value, clazz);
    }


    public void set(String catalog, Object key, Object value) {
        set(PREFIX + catalog + ":" + key, JSON.toJSONString(value),OBJECT_EXPIRE_S);
    }

}
