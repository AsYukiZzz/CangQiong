package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrdersService {

    /**
     * 提交订单
     *
     * @param ordersSubmitDTO 订单信息封装
     * @return 订单信息回执
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO 订单支付信息封装
     * @return 支付单信息返回
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo 订单号
     */
    void paySuccess(String outTradeNo);

    /**
     * 分页查询历史订单
     *
     * @param ordersPageQueryDTO 分页查询条件封装
     * @return 分页查询结果
     */
    PageResult getHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 管理端分页查询订单
     *
     * @param ordersPageQueryDTO 分页查询条件
     * @return 符合条件订单集合
     */
    PageResult listOrdersForManagement(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 管理端统计各状态订单数量
     *
     * @return 统计结果
     */
    OrderStatisticsVO countOrdersByStatus();

    /**
     * 管理端根据订单Id查询订单信息
     *
     * @param id 订单Id
     * @return 订单信息
     */
    OrderVO getOrderById(String id);

    /**
     * 管理端接单
     *
     * @param ordersConfirmDTO 订单Id封装
     */
    void takeOrder(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 管理端拒单
     *
     * @param ordersRejectionDTO 拒单信息封装
     */
    void rejectOrder(OrdersRejectionDTO ordersRejectionDTO);

    /**
     * 管理端取消订单
     *
     * @param ordersCancelDTO 取消订单信息封装
     */
    void cancelOrder(OrdersCancelDTO ordersCancelDTO);

    /**
     * 派送订单
     *
     * @param id 订单Id
     */
    void deliveryOrder(String id);

    /**
     * 完成订单
     *
     * @param id 订单Id
     */
    void completeOrder(String id);

    /**
     * 用户端再来一单
     *
     * @param id 订单Id
     */
    void repetitionOrder(String id);
}
