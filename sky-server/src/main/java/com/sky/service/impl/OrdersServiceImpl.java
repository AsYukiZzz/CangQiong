package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.context.CurrentHolder;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrdersService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;

    /**
     * 提交订单
     *
     * @param ordersSubmitDTO 订单信息封装
     * @return 订单信息回执
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {

        //获取当前用户ID
        Long userId = CurrentHolder.getCurrentHolder();
        //获取当前时间
        LocalDateTime now = LocalDateTime.now();
        //生成订单号
        String orderNumber = String.valueOf(System.currentTimeMillis());

        //根据地址ID查询地址信息
        AddressBook addressInfo = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressInfo == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        //根据用户ID查询购物车信息
        ShoppingCart condition = ShoppingCart.builder().userId(userId).build();
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.searchItem(condition);
        if (shoppingCarts == null || shoppingCarts.isEmpty()) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //封装Orders对象
        Orders order = Orders.builder()
                .status(Orders.PENDING_PAYMENT)             //设置订单状态为待付款状态
                .userId(userId)                             //设置用户ID
                .orderTime(now)                             //设置下单时间
                .payStatus(Orders.UN_PAID)                  //设置付款状态为未付款
                .phone(addressInfo.getPhone())              //设置用户手机号
                .address(addressInfo.getDetail())           //设置详细地址
                .consignee(addressInfo.getConsignee())      //设置用户名字
                .number(orderNumber)                        //设置订单号
                .build();

        //将OrderDTO封装的部分数据拷贝到order中
        BeanUtils.copyProperties(ordersSubmitDTO, order);

        //提交订单数据到数据库
        ordersMapper.submitOrder(order);

        //获取订单ID
        Long orderId = order.getId();

        //提交订单细节数据到数据库（购物车->订单细节表）

        List<OrderDetail> orderDetails = new ArrayList<>();
        for (ShoppingCart shoppingCart : shoppingCarts) {
            //封装单个OrderDetail对象
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            orderDetail.setOrderId(orderId);
            //添加到集合中
            orderDetails.add(orderDetail);
        }

        //批量添加到数据库
        orderDetailMapper.addOrderDetail(orderDetails);

        //清空购物车
        shoppingCartMapper.deleteItem(condition);

        //返回VO
        return OrderSubmitVO.builder()
                .id(orderId)
                .orderAmount(ordersSubmitDTO.getAmount())
                .orderNumber(orderNumber)
                .orderTime(now)
                .build();
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO 支付单信息封装
     * @return 支付单信息返回
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = CurrentHolder.getCurrentHolder();
        User user = userMapper.getUserById(String.valueOf(userId));

        //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal("0.01"), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo 订单号
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = ordersMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        ordersMapper.update(orders);
    }

    /**
     * 分页查询历史订单
     *
     * @param ordersPageQueryDTO 分页查询条件封装
     * @return 分页查询结果
     */
    @Override
    public PageResult getHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        //设置用户ID
        ordersPageQueryDTO.setUserId(CurrentHolder.getCurrentHolder());

        //执行分页查询
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        OrderVO orderVO = new OrderVO();
        List<OrderVO> orderVOList = ordersMapper.getOrders(ordersPageQueryDTO);

        //将orderList向下转型为PageInfo
        PageInfo<OrderVO> pageInfo = new PageInfo<>(orderVOList);

        for (OrderVO o : orderVOList) {
            Long orderId = o.getId();
            List<OrderDetail> orderDetailList = orderDetailMapper.getOrderDetailByOrderId(orderId);
            orderVO.setOrderDetailList(orderDetailList);
        }

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 管理端分页查询订单
     *
     * @param ordersPageQueryDTO 分页查询条件
     * @return 符合条件订单集合
     */
    @Override
    public PageResult listOrdersForManagement(OrdersPageQueryDTO ordersPageQueryDTO) {

        //执行分页查询
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        List<OrderVO> ordersList = ordersMapper.getOrders(ordersPageQueryDTO);

        //获取记录数与集合并返回
        PageInfo<OrderVO> pageInfo = new PageInfo<>(ordersList);
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 管理端统计各状态订单数量
     *
     * @return 统计结果
     */
    @Override
    public OrderStatisticsVO countOrdersByStatus() {
        List<OrderStatusCountModel> countList = ordersMapper.countOrderByStatus();
        OrderStatisticsVO statisticsVO = new OrderStatisticsVO();

        for (OrderStatusCountModel model : countList) {
            if (model.getStatus().equals(Orders.CONFIRMED)) {
                statisticsVO.setConfirmed(model.getCount());
            } else if (model.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
                statisticsVO.setToBeConfirmed(model.getCount());
            } else if (model.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)) {
                statisticsVO.setDeliveryInProgress(model.getCount());
            }
        }

        return statisticsVO;
    }

    /**
     * 管理端根据订单Id查询订单信息
     *
     * @param id 订单Id
     * @return 订单信息
     */
    @Override
    public OrderVO getOrderById(String id) {
        //根据订单ID查询订单内容
        OrderVO orderVO = ordersMapper.getOrderById(id);

        //查询订单细节信息
        orderVO.setOrderDetailList(orderDetailMapper.getOrderDetailByOrderId(Long.valueOf(id)));

        return orderVO;
    }

    /**
     * 管理端接单
     *
     * @param ordersConfirmDTO 订单Id封装
     */
    @Override
    public void takeOrder(OrdersConfirmDTO ordersConfirmDTO) {
        //查询原订单信息以进行校验
        OrderVO order = ordersMapper.getOrderById(String.valueOf(ordersConfirmDTO.getId()));

        //校验订单状态
        verifyOrder(order, Orders.TO_BE_CONFIRMED);

        //更改订单状态
        order.setStatus(Orders.CONFIRMED);
        ordersMapper.updateOrders(order);
    }

    /**
     * 管理端拒单
     *
     * @param ordersRejectionDTO 拒单信息封装
     */
    @Override
    public void rejectOrder(OrdersRejectionDTO ordersRejectionDTO) {
        //查询原订单信息以进行校验
        OrderVO order = ordersMapper.getOrderById(String.valueOf(ordersRejectionDTO.getId()));

        //校验订单状态
        verifyOrder(order, Orders.TO_BE_CONFIRMED);

        //更改订单信息
        //todo 拒单与取消订单区别？是否需要与拒绝原因也进行校验？
        order.setStatus(Orders.CANCELLED);
        order.setCancelTime(LocalDateTime.now());
        order.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        ordersMapper.updateOrders(order);
    }

    /**
     * 管理端取消订单
     *
     * @param ordersCancelDTO 取消订单信息封装
     */
    @Override
    public void cancelOrder(OrdersCancelDTO ordersCancelDTO) {
        //查询原订单信息以进行校验
        OrderVO order = ordersMapper.getOrderById(String.valueOf(ordersCancelDTO.getId()));

        //校验订单状态
        verifyOrder(order, Orders.CONFIRMED, Orders.DELIVERY_IN_PROGRESS, Orders.COMPLETED);

        //更改订单信息
        order.setStatus(Orders.CANCELLED);
        order.setCancelReason(ordersCancelDTO.getCancelReason());
        order.setCancelTime(LocalDateTime.now());
        ordersMapper.updateOrders(order);
    }

    /**
     * 派送订单
     *
     * @param id 订单Id
     */
    @Override
    public void deliveryOrder(String id) {
        //查询原订单信息以进行校验
        OrderVO order = ordersMapper.getOrderById(id);

        //校验订单状态
        verifyOrder(order, Orders.CONFIRMED);

        //校验是否到达配送时间
        //todo 接入导航SDK后应计算具体送达时间再行比较？是否应该存在这个校验项目？
        if (order.getDeliveryStatus() == 0 && order.getEstimatedDeliveryTime().minusMinutes(15).isAfter(LocalDateTime.now())){
            throw new OrderBusinessException(MessageConstant.DELIVERY_TIME_TOO_EARLY);
        }

        //更改订单信息
        order.setStatus(Orders.DELIVERY_IN_PROGRESS);
        ordersMapper.updateOrders(order);
    }

    /**
     * 完成订单
     *
     * @param id 订单Id
     */
    @Override
    public void completeOrder(String id) {
        //查询原订单信息以进行校验
        OrderVO order = ordersMapper.getOrderById(id);

        //校验订单状态
        verifyOrder(order, Orders.DELIVERY_IN_PROGRESS);

        //更改订单信息
        order.setStatus(Orders.COMPLETED);
        order.setDeliveryTime(LocalDateTime.now());
        ordersMapper.updateOrders(order);
    }

    /**
     * 校验订单状态
     *
     * @param order             订单
     * @param targetOrderStatus 预期订单状态，可变参数
     */
    private void verifyOrder(OrderVO order, int... targetOrderStatus) {
        //订单为null，则抛出订单不存在异常
        if (order == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        //订单状态不是预期状态，则抛出“订单状态异常”异常
        for (int status : targetOrderStatus) {
            if (order.getStatus().equals(status)) {
                return;
            }
        }

        throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
    }

}
