package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderSubmitVO;

public interface OrdersService {

    /**
     * 提交订单
     * @param ordersSubmitDTO 订单信息封装
     * @return 订单信息回执
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);
}
