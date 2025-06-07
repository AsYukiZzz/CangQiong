package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {


    void addDishFlavors(List<DishFlavor> dishFlavors);

    /**
     * 批量删除指定ID菜品的口味
     * @param ids 待删除菜品ID
     */
    void deleteDishFlavorByIds(List<String> ids);

    @Select("select id, dish_id, name, value from dish_flavor where dish_id = #{id}")
    List<DishFlavor> getDishFlavorById(@Param("id") String id);
}
