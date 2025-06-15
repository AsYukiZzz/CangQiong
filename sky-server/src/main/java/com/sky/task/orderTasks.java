package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrdersMapper;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class orderTasks {

    @Autowired
    private OrdersMapper ordersMapper;

    /**
     * 每晚四点自动将配送中订单转换为完成订单，正常情况下本事件段性能冗余，可以使用单SQL语句遍历
     */
    @Scheduled(cron = "0 0 4 * * ? ")
    public void completeOrdersAuto() {
        Orders searchInfo = Orders.builder().status(Orders.DELIVERY_IN_PROGRESS).build();
        List<OrderVO> orders = ordersMapper.getOrders(searchInfo);
        LocalDateTime now = LocalDateTime.now();

        for (OrderVO order : orders) {
            //检测预期送达时间是否早于当前时间
            if (order.getEstimatedDeliveryTime().isBefore(now)) {
                //更改订单信息
                order.setStatus(Orders.COMPLETED);
                order.setDeliveryTime(LocalDateTime.now());
                ordersMapper.updateOrders(order);
            }
        }
    }
}
