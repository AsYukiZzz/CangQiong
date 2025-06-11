package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

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
     *
     * @param ids 待删除菜品ID
     */
    void deleteDishes(List<String> ids);

    /**
     * 根据ID查询菜品
     *
     * @param id 待查询菜品ID
     * @return 菜品信息
     */
    DishVO getDishById(String id);

    /**
     * 根据ID更改菜品启用状态
     *
     * @param dishDTO 待更改信息封装
     */
    void updateDishStatus(DishDTO dishDTO);

    /**
     * 根据ID更改菜品信息
     *
     * @param dishDTO 待更改信息封装
     */
    void updateDish(DishDTO dishDTO);

    List<DishVO> getDishByList(String categoryId);

    /**
     * 条件查询菜品和口味
     *
     * @param dishDTO 查询条件封装
     * @return 符合条件的菜品信息集合
     */
    List<DishVO> listWithFlavor(DishDTO dishDTO);
}
