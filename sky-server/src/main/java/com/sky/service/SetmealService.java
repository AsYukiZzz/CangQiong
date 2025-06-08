package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;

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
    void addSetmeal(SetmealDTO setmealDTO);

    /**
     * 批量删除套餐
     * @param ids 套餐ID集合
     */
    void deleteSetmeal(List<String> ids);
}
