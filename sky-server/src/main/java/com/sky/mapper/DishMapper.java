package com.sky.mapper;

import com.sky.anno.Autofill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 添加菜品
     * @param dish 菜品对象
     */
    @Autofill(OperationType.INSERT)
    void addDish(Dish dish);


    /**
     * 菜品分页查询
     * @param dishPageQueryDTO 菜品分页查询数据封装
     * @return 菜品集合
     */
    List<DishVO> getDishByPage(DishPageQueryDTO dishPageQueryDTO);
}
