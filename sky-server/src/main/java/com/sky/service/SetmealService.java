package com.sky.service;

import com.sky.anno.Autofill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO 套餐分页查询参数封装
     * @return 套餐分页查询结果
     */
    PageResult getSetmealByPage(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 添加套餐
     * @param setmealDTO 套餐信息封装
     */
    @Autofill(OperationType.INSERT)
    void addSetmeal(SetmealDTO setmealDTO);

    /**
     * 批量删除套餐
     * @param ids 套餐ID集合
     */
    void deleteSetmeal(List<String> ids);

    /**
     * 根据ID查询套餐信息
     * @param id 套餐ID
     * @return 套餐信息
     */
    SetmealVO getSetmealById(String id);

    /**
     * 根据ID更改套餐信息
     * @param setmealDTO 套餐信息封装
     */
    void updateSetmeal(SetmealDTO setmealDTO);

    /**
     * 根据ID修改套餐启用状态
     * @param status 修改后状态
     * @param id 套餐ID
     */
    void updateSetmealStatus(Integer status, Long id);

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);
}
