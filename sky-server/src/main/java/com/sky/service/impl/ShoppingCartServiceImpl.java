package com.sky.service.impl;

import com.sky.context.CurrentHolder;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    /**
     * 向购物车添加内容
     *
     * @param shoppingCartDTO 添加内容数据封装
     */
    @Override
    public void addItem(ShoppingCartDTO shoppingCartDTO) {
        //获取当前操作用户
        Long currentHolderId = getCurrentHolder();

        ShoppingCart shoppingCart = new ShoppingCart();

        //将shoppingCartDTO的属性拷贝到shoppingCart中
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);

        //赋值UserID
        shoppingCart.setUserId(currentHolderId);

        //检测数据库是否存在当前用户添加的同种物品
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.searchItem(shoppingCart);

        //数据库存在当前用户添加的同种物品
        if (shoppingCartList != null && !shoppingCartList.isEmpty()) {
            ShoppingCart scItem = shoppingCartList.get(0);
            //数量加一，并写回数据库
            scItem.setNumber(scItem.getNumber() + 1);
            shoppingCartMapper.updateItem(scItem);
            return;
        }

        //数据库不存在当前用户添加的同种物品
        shoppingCart = ShoppingCart.builder()
                .userId(currentHolderId)
                .createTime(LocalDateTime.now())
                .number(1)
                .build();

        //声明ID，可能是dishId或setmealId
        Long id;

        //获取菜品ID，若菜品ID为null，说明添加了套餐而非菜品
        if ((id = shoppingCartDTO.getDishId()) != null) {
            DishVO dish = dishMapper.getDishById(String.valueOf(id));
            shoppingCart.setDishId(dish.getId());
            shoppingCart.setName(dish.getName());
            shoppingCart.setImage(dish.getImage());
            shoppingCart.setAmount(dish.getPrice());
        } else {
            id = shoppingCartDTO.getSetmealId();
            SetmealVO setmeal = setmealMapper.getSetmealById(String.valueOf(id));
            shoppingCart.setSetmealId(setmeal.getId());
            shoppingCart.setName(setmeal.getName());
            shoppingCart.setImage(setmeal.getImage());
            shoppingCart.setAmount(setmeal.getPrice());
        }

        //添加到数据库
        shoppingCartMapper.addItem(shoppingCart);
    }

    /**
     * 查询购物车内容
     *
     * @return 购物车内容封装
     */
    @Override
    public List<ShoppingCart> listItem() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(getCurrentHolder());
        return shoppingCartMapper.searchItem(shoppingCart);
    }

    /**
     * 清空购物车
     */
    @Override
    public void clearItem() {
        shoppingCartMapper.deleteItem(ShoppingCart.builder().userId(getCurrentHolder()).build());
    }

    /**
     * 减少购物车指定物品的数量
     *
     * @param shoppingCartDTO 物品信息封装
     */
    @Override
    public void subItem(ShoppingCartDTO shoppingCartDTO) {

        ShoppingCart shoppingCart = new ShoppingCart();

        //将shoppingCartDTO的属性拷贝到shoppingCart中
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);

        //赋值UserID
        shoppingCart.setUserId(getCurrentHolder());

        //检测数据库是否存在当前用户添加的同种物品
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.searchItem(shoppingCart);

        ShoppingCart scItem = shoppingCartList.get(0);
        int number;

        if ((number = (scItem.getNumber() - 1)) > 0) {
            scItem.setNumber(number);
            shoppingCartMapper.updateItem(scItem);
        } else {
            shoppingCartMapper.deleteItem(shoppingCart);
        }
    }

    /**
     * 获取当前用户ID
     *
     * @return 用户ID
     */
    private Long getCurrentHolder() {
        return CurrentHolder.getCurrentHolder();
    }
}
