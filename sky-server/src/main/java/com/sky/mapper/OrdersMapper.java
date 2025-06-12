package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper {

    /**
     * 提交订单
     * @param order 订单信息封装
     */
    void submitOrder(Orders order);
}
