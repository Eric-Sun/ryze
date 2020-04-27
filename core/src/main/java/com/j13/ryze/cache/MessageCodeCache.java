package com.j13.ryze.cache;

import com.j13.poppy.Cache;
import com.j13.poppy.JedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageCodeCache implements Cache {
    // 默认短信验证码的有效期为10分钟
    private static int MESSAGE_CODE_EXPIRE_S = 10 * 60;

    @Autowired
    JedisManager jedisManager;

    /**
     * 短信验证码放入redis中
     *
     * @param mobile
     * @param messageCode
     */
    public void setMessageCode(String mobile, String messageCode) {
        jedisManager.set(PREFIX + "code:" + mobile, messageCode, MESSAGE_CODE_EXPIRE_S);
    }


    /**
     * 尝试从redis中读取messageCode，如果失败则说明已经失效，返回null
     *
     * @param mobile
     * @return
     */
    public String getMessageCode(String mobile) {
        String messageCode = jedisManager.get(PREFIX + "code:" + mobile);
        if (messageCode == null) {
            return null;
        } else {
            return messageCode;
        }
    }

    public void deleteMessageCode(String mobile){
        jedisManager.delete(PREFIX + "code:" + mobile);
    }

}
