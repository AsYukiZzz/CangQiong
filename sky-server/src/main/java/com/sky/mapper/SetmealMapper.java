package com.sky.mapper;

import com.sky.anno.Autofill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id 分类ID
     * @return 套餐数量
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO 套餐分页查询参数
     * @return 套餐分页查询结果
     */
    List<Setmeal> getSetmealByPage(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 添加套餐
     * @param setmeal 套餐信息封装
     */
    @Autofill(OperationType.INSERT)
    void addSetmeal(Setmeal setmeal);
}
