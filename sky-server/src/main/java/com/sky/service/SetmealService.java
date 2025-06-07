package com.sky.service;

import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;

public interface SetmealService {

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO 套餐分页查询参数封装
     * @return 套餐分页查询结果
     */
    PageResult getSetmealByPage(SetmealPageQueryDTO setmealPageQueryDTO);

}
