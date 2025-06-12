package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.CurrentHolder;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrdersMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrdersService;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 提交订单
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
        BeanUtils.copyProperties(ordersSubmitDTO,order);

        //提交订单数据到数据库
        ordersMapper.submitOrder(order);

        //获取订单ID
        Long orderId = order.getId();

        //提交订单细节数据到数据库（购物车->订单细节表）

        List<OrderDetail> orderDetails = new ArrayList<>();
        for (ShoppingCart shoppingCart : shoppingCarts) {
            //封装单个OrderDetail对象
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart,orderDetail);
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
}
