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
}
