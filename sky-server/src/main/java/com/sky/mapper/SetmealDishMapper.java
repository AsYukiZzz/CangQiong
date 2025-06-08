package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品ID获取套餐信息
     * @param ids 菜品ID
     * @return 套餐列表
     */
    List<SetmealDish> getSetmealByDishId(List<String> ids);

    /**
     * 添加套餐-菜品关系
     * @param setmealDishList 谭灿-菜品关系集合
     */
    void addSetmealDish(List<SetmealDish> setmealDishList);
}
