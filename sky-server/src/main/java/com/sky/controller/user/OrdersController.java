package com.sky.controller.user;

import com.sky.context.CurrentHolderInfo;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrdersService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单相关接口
 */
@Slf4j
@RestController("userOrdersController")
@RequestMapping("/user/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    /**
     * 提交订单
     *
     * @param ordersSubmitDTO 订单信息封装
     * @return 订单信息回执
     */
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submitOrder(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户提交订单，{}", ordersSubmitDTO);
        return Result.success(ordersService.submitOrder(ordersSubmitDTO));
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO 订单支付信息封装
     * @return 返回交易单信息
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = ordersService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    /**
     * 分页查询历史订单
     *
     * @param ordersPageQueryDTO 分页查询条件封装
     * @return 分页查询结果
     */
    @GetMapping("/historyOrders")
    public Result<PageResult> getHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("用户id={}，请求查询历史订单，{}", CurrentHolderInfo.getCurrentHolder(), ordersPageQueryDTO);
        return Result.success(ordersService.getHistoryOrders(ordersPageQueryDTO));
    }

    /**
     * 用户端端根据订单Id查询订单信息
     *
     * @param id 订单Id
     * @return 订单信息
     */
    @GetMapping("/details/{id}")
    public Result<OrderVO> getOrderById(@PathVariable String id) {
        log.info("用户端查询订单详情，订单id={}", id);
        return Result.success(ordersService.getOrderById(id));
    }

    /**
     * 用户端取消订单
     *
     * @param ordersCancelDTO 取消订单信息封装
     * @return 成功取消订单结果返回
     */
    @PutMapping("/cancel/{id}")
    public Result<String> cancelOrder(OrdersCancelDTO ordersCancelDTO) throws Exception {
        log.info("用户取消订单，订单id={}", ordersCancelDTO.getId());
        ordersCancelDTO.setCancelReason("用户取消");
        ordersService.cancelOrder(ordersCancelDTO);
        return Result.success();
    }

    /**
     * 用户端再来一单
     *
     * @param id 订单Id
     * @return 操作成功结果返回
     */
    @PostMapping("/repetition/{id}")
    public Result<String> repetitionOrder(@PathVariable String id) {
        log.info("用户端再来一单，用户id={}，订单id={}", CurrentHolderInfo.getCurrentHolder(), id);
        ordersService.repetitionOrder(id);
        return Result.success();
    }
}
