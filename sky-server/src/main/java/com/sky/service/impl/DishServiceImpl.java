package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDish(DishDTO dishDTO) {
        //构造菜品信息对象
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dish.setStatus(StatusConstant.DISABLE);

        //向数据库添加菜品信息
        dishMapper.addDish(dish);

        //获取菜品ID
        Long dishId = dish.getId();

        //为菜品口味设置对应的归属菜品ID
        List<DishFlavor> dishFlavors = dishDTO.getFlavors();
        for (DishFlavor df : dishFlavors) {
            df.setDishId(dishId);
        }

        //向数据库中添加菜品口味信息
        dishFlavorMapper.addDishFlavors(dishFlavors);
    }
}
