package com.sky.controller.admin;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrdersService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/order")
public class OrderController {

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
}
