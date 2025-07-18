package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品ID获取套餐菜品关联信息
     * @param ids 菜品ID
     * @return 套餐菜品关联信息
     */
    List<SetmealDish> getSetmealDishByDishId(List<String> ids);

    /**
     * 添加套餐-菜品关系
     * @param setmealDishList 谭灿-菜品关系集合
     */
    void addSetmealDish(List<SetmealDish> setmealDishList);

    /**
     * 根据套餐ID获取套餐菜品关联信息
     * @param id 套餐ID
     * @return 套餐菜品关联信息
     */
    @Select("select id, setmeal_id, dish_id, name, price, copies from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getSetmealDishBySetmealId(@Param("id") String id);

    /**
     * 根据套餐ID删除套餐菜品关联信息
     * @param ids 套餐ID集合
     */

    void deleteSetmealDishBySetmealId(List<String> ids);

    @Update("update setmeal_dish set name=#{name},price=#{price} where dish_id=#{dishId}")
    void updateSetmealDishByDishId(SetmealDish setmealDish);
}
