package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    /**
     * 向购物车添加内容
     *
     * @param shoppingCartDTO 添加内容数据封装
     */
    void addItem(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查询购物车内容
     *
     * @return 购物车内容封装
     */
    List<ShoppingCart> listItem();

    /**
     * 清空购物车
     */
    void clearItem();

    /**
     * 减少购物车指定物品的数量
     *
     * @param shoppingCartDTO 物品信息封装
     */
    void subItem(ShoppingCartDTO shoppingCartDTO);
}
