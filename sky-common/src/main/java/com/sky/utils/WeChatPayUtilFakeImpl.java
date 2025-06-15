package com.sky.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WeChatPayUtilFakeImpl implements WeChatPayUtil {
    @Override
    public JSONObject pay(String orderNum, BigDecimal total, String description, String openid) throws Exception {

        //生成假JsonObject
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nonceStr", "abc");
        jsonObject.put("paySign", "abc");
        jsonObject.put("timeStamp", "abc");
        jsonObject.put("signType", "abc");
        jsonObject.put("packageStr", "abc");

        //此处直接修改订单状态


        return jsonObject;
    }

    @Override
    public String refund(String outTradeNo, String outRefundNo, BigDecimal refund, BigDecimal total) throws Exception {
        return "{ WeChatPayUtilFakeImpl:已经退款 }";
    }
}
