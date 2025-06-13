package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    /**
     * 批量添加订单细节信息
     * @param orderDetails 订单细节信息封装
     */
    void addOrderDetail(List<OrderDetail> orderDetails);

    /**
     * 根据订单ID查询订单细节信息
     * @param orderId 订单ID
     * @return 订单细节信息集合
     */
    @Select("select id, name, image, order_id, dish_id, setmeal_id, dish_flavor, number, amount from order_detail where order_id = #{orderId}")
    List<OrderDetail> getOrderDetailByOrderId(Long orderId);
}
