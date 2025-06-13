package com.sky.controller.admin;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
     * @param ordersPageQueryDTO 分页查询条件
     * @return 符合条件订单集合
     */
    @GetMapping("/conditionSearch")
    public Result<PageResult> listOrdersForManagement(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("管理端分页查询订单，{}", ordersPageQueryDTO);
        return Result.success(ordersService.listOrdersForManagement(ordersPageQueryDTO));
    }
}
