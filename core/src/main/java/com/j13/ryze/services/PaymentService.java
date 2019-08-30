package com.j13.ryze.services;

import com.j13.ryze.core.Constants;
import com.j13.ryze.utils.DateUtil;
import com.j13.ryze.utils.InternetUtil;
import com.j13.ryze.vos.PayInfoVO;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Date;
import java.util.Random;

public class PaymentService {


    public void createUnifiedorder(int userId, String openId, int totalFee) {

        // 生成订单
        String orderNo = genOrderNo();
        String nonceStr = genNonceStr();
        String clientIp = InternetUtil.getLocalIp();

        PayInfoVO payInfoVO = createPayInfo(openId, clientIp, nonceStr, orderNo, totalFee);
    }


    private String genNonceStr() {
        return RandomStringUtils.randomAlphanumeric(20);
    }

    private PayInfoVO createPayInfo(String openId, String clientIP, String nonceStr, String orderNo, int totalFee) {
        PayInfoVO payInfo = new PayInfoVO();
        payInfo.setApp_id(Constants.Wechat.APPID);
        payInfo.setMch_id(Constants.Wechat.MchId);
        payInfo.setNonce_str(nonceStr);
        payInfo.setSign_type("MD5");  //默认即为MD5
        payInfo.setBody("JSAPI支付测试");
        payInfo.setOut_trade_no(orderNo);
        payInfo.setTotal_fee(totalFee);
        payInfo.setSpbill_create_ip(clientIP);
        payInfo.setNotify_url(Constants.PAYMENT.URL_NOTIFY);
        payInfo.setTrade_type("JSAPI");
        payInfo.setOpenid(openId);
        return payInfo;
    }

    private String genOrderNo() {
        Random r = new Random();
        return "";

    }


}
