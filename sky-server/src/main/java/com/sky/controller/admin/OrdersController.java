package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrdersService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    /**
     * 管理端分页查询订单
     *
     * @param ordersPageQueryDTO 分页查询条件
     * @return 符合条件订单集合
     */
    @GetMapping("/conditionSearch")
    public Result<PageResult> listOrdersForManagement(OrdersPageQueryDTO ordersPageQueryDTO) {
        //todo 待派送订单菜品不显示
        log.info("管理端分页查询订单，{}", ordersPageQueryDTO);
        return Result.success(ordersService.listOrdersForManagement(ordersPageQueryDTO));
    }

    /**
     * 管理端统计各状态订单数量
     *
     * @return 统计结果
     */
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> countOrdersByStatus() {
        log.info("管理端统计各状态订单数量");
        return Result.success(ordersService.countOrdersByStatus());
    }

    /**
     * 管理端根据订单Id查询订单信息
     *
     * @param id 订单Id
     * @return 订单信息
     */
    @GetMapping("/details/{id}")
    public Result<OrderVO> getOrderById(@PathVariable String id) {
        log.info("管理端查询订单详情，订单id={}", id);
        return Result.success(ordersService.getOrderById(id));
    }

    /**
     * 管理端接单
     *
     * @param ordersConfirmDTO 订单Id封装
     * @return 成功接单结果返回
     */
    @PutMapping("/confirm")
    public Result<String> takeOrders(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        log.info("管理端接单，订单id={}", ordersConfirmDTO.getId());
        ordersService.takeOrder(ordersConfirmDTO);
        return Result.success();
    }

    /**
     * 管理端拒单
     *
     * @param ordersRejectionDTO 拒单信息封装
     * @return 成功拒单结果返回
     */
    @PutMapping("/rejection")
    public Result<String> rejectOrder(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        log.info("管理端拒单，订单id={}", ordersRejectionDTO.getId());
        ordersService.rejectOrder(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * 管理端取消订单
     *
     * @param ordersCancelDTO 取消订单信息封装
     * @return 成功取消订单结果返回
     */
    @PutMapping("/cancel")
    public Result<String> cancelOrder(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        log.info("管理端取消订单，订单id={}", ordersCancelDTO.getId());
        ordersService.cancelOrder(ordersCancelDTO);
        return Result.success();
    }

    /**
     * 派送订单
     *
     * @param id 订单Id
     * @return 成功开始派送结果返回
     */
    @PutMapping("/delivery/{id}")
    public Result<String> deliveryOrder(@PathVariable String id) {
        log.info("订单开始派送，订单id={}", id);
        ordersService.deliveryOrder(id);
        return Result.success();
    }

    /**
     * 完成订单
     *
     * @param id 订单Id
     * @return 成功完成订单结果返回
     */
    @PutMapping("/complete/{id}")
    public Result<String> completeOrder(@PathVariable String id) {
        log.info("订单完成，订单id={}", id);
        ordersService.completeOrder(id);
        return Result.success();
    }
}
