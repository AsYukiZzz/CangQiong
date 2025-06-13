package com.sky.service;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;

public interface OrdersService {

    /**
     * 提交订单
     * @param ordersSubmitDTO 订单信息封装
     * @return 订单信息回执
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO 订单支付信息封装
     * @return 支付单信息返回
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo 订单号
     */
    void paySuccess(String outTradeNo);

    /**
     * 分页查询历史订单
     * @param ordersPageQueryDTO 分页查询条件封装
     * @return 分页查询结果
     */
    PageResult getHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 管理端分页查询订单
     * @param ordersPageQueryDTO 分页查询条件
     * @return 符合条件订单集合
     */
    PageResult listOrdersForManagement(OrdersPageQueryDTO ordersPageQueryDTO);
}
