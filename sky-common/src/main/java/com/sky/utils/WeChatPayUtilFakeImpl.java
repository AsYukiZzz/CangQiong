package com.sky.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WeChatPayUtilFakeImpl implements WeChatPayUtil {
    @Override
    public JSONObject pay(String orderNum, BigDecimal total, String description, String openid) throws Exception {
        return null;
    }

    @Override
    public String refund(String outTradeNo, String outRefundNo, BigDecimal refund, BigDecimal total) throws Exception {
        return "{ WeChatPayUtilFakeImpl:已经退款 }";
    }
}
