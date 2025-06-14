package com.sky.utils;

import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;

public interface WeChatPayUtil {

    /**
     * 小程序支付
     *
     * @param orderNum    商户订单号
     * @param total       金额，单位 元
     * @param description 商品描述
     * @param openid      微信用户的openid
     */
    JSONObject pay(String orderNum, BigDecimal total, String description, String openid) throws Exception;

    /**
     * 申请退款
     *
     * @param outTradeNo    商户订单号
     * @param outRefundNo   商户退款单号
     * @param refund        退款金额
     * @param total         原订单金额
     */
    String refund(String outTradeNo, String outRefundNo, BigDecimal refund, BigDecimal total) throws Exception;
}
