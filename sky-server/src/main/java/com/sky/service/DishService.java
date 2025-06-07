package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

public interface DishService {

    void addDish(DishDTO dishDTO);

    /**
     * 菜品分页查询Service层接口
     *
     * @param dishPageQueryDTO 菜品分页条件查询数据封装
     * @return 返回结果
     */
    PageResult getDishByPage(DishPageQueryDTO dishPageQueryDTO);


    /**
     * 批量删除菜品
     * @param ids 待删除菜品ID
     */
    void deleteDishes(List<String> ids);
}
