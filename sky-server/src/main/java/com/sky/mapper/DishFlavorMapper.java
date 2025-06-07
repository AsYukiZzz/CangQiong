package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {


    void addDishFlavors(List<DishFlavor> dishFlavors);

    /**
     * 批量删除指定ID菜品的口味
     * @param ids 待删除菜品ID
     */
    void deleteDishFlavorByIds(List<String> ids);
}
