package com.j13.garen.services;

import com.j13.poppy.config.PropertiesConfiguration;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OrderService {

    private static Random random = new Random();

    public String genOrderNumber() {
        int end = random.nextInt(9);
        return System.currentTimeMillis() + "" + end;
    }

    public String getStatusStr(int status){
        return PropertiesConfiguration.getInstance().getStringValue("status.0");
    }
}
