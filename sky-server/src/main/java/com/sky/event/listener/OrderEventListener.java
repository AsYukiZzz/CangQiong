package com.sky.event.listener;

import com.sky.entity.Orders;
import com.sky.event.OrderCreatedEvent;
import com.sky.event.OrderPaidEvent;
import com.sky.mapper.OrdersMapper;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Component
public class OrderEventListener {

    //TODO 此处存在问题：ConcurrentHashMap中的数据并没有做持久化处理，重启Map的任务会被取消

    ConcurrentHashMap<Long, ScheduledFuture<?>> tasks = new ConcurrentHashMap<>();

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private OrdersMapper ordersMapper;

    @EventListener
    public void execute(OrderCreatedEvent event) {
        Long orderId = event.getOrderId();
        log.info("开始监听订单状态，订单Id={}",orderId);
        //添加定时15Min任务到ConcurrentMap中
        tasks.put(
                orderId,
                taskScheduler.schedule(
                    () -> cancelUnPaidTimeOutOrder(orderId),
                    Instant.now().plusSeconds(900) // 15分钟后
        ));
    }

    @EventListener
    public void execute(OrderPaidEvent event) {
        String orderNumber = event.getOrderNumber();
        log.info("订单{}已付款，取消监听",orderNumber);

        //获取订单Id
        Long id = ordersMapper.getByNumber(orderNumber).getId();

        if (tasks.containsKey(id)) {

            //取消任务，参数mayInterruptIfRunning为false代表任务若在执行则允许任务执行
            tasks.get(id).cancel(false);

            //移除任务
            tasks.remove(id);
        }
    }

    /**
     * 超时未付款订单取消
     * @param orderId 订单Id
     */
    private void cancelUnPaidTimeOutOrder(Long orderId) {
        //封装更改数据
        OrderVO order = ordersMapper.getOrderById(String.valueOf(orderId));

        //检测订单是否支付
        if (order.getPayStatus().equals(Orders.PAID)) {
            return;
        }

        order.setStatus(Orders.CANCELLED);
        order.setCancelTime(LocalDateTime.now());
        order.setCancelReason("用户未付款");

        //更新订单状态
        ordersMapper.updateOrders(order);
    }


}
