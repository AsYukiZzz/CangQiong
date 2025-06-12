package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 向购物车添加物品（同用户同物品第一次添加）
     *
     * @param shoppingCart 物品信息封装
     */
    void addItem(ShoppingCart shoppingCart);

    /**
     * 根据条件信息搜索购物车物品
     *
     * @param shoppingCart 条件信息封装
     * @return 符合条件的购物车条目
     */
    List<ShoppingCart> searchItem(ShoppingCart shoppingCart);

    /**
     * 增加购物车指定用户指定物品的数量（+1，同用户同物品第n次添加（n>1））
     *
     * @param shoppingCart 物品信息封装
     */
    void updateItem(ShoppingCart shoppingCart);

    /**
     * 根据给定条件删除购物车条目
     *
     * @param shoppingCart 条件封装
     */
    void deleteItem(ShoppingCart shoppingCart);
}
