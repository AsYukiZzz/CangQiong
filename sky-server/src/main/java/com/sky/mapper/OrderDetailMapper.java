package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    /**
     * 批量添加订单细节信息
     * @param orderDetails 订单细节信息封装
     */
    void addOrderDetail(List<OrderDetail> orderDetails);
}
