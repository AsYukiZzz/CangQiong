package com.sky.mapper;

import com.sky.dto.OrderStatusCountModel;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface OrdersMapper {

    /**
     * 提交订单
     *
     * @param order 订单信息封装
     */
    void submitOrder(Orders order);

    /**
     * 根据订单号查询订单
     *
     * @param orderNumber 订单号
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     *
     * @param orders 订单信息封装
     */
    void update(Orders orders);

    /**
     * 根据条件查询订单信息
     *
     * @param ordersPageQueryDTO 查询条件封装
     * @return 查询结果
     */
    List<OrderVO> getOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 管理端统计各状态订单数量
     *
     * @return 统计结果
     */
    @Select("select status,count(*) count from orders group by status")
    List<OrderStatusCountModel> countOrderByStatus();

    /**
     * 管理端根据订单Id查询订单信息
     *
     * @param id 订单Id
     * @return 订单信息
     */
    @Select("select id, number, status, user_id, address_book_id, order_time, checkout_time, pay_method, pay_status, amount, remark, phone, address, user_name, consignee, cancel_reason, rejection_reason, cancel_time, estimated_delivery_time, delivery_status, delivery_time, pack_amount, tableware_number, tableware_status from orders where id = #{id}")
    OrderVO getOrderById(@Param("id") String id);
}
