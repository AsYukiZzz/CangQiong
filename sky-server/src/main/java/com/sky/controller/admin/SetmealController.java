package com.sky.controller.admin;

import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 套餐管理
 */
@Slf4j
@RestController
@RequestMapping("/admin/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO 套餐分页查询参数封装
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public Result<PageResult> getSetmealByPage(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("套餐分页查询，{}", setmealPageQueryDTO);
        return Result.success(setmealService.getSetmealByPage(setmealPageQueryDTO));
    }

}
