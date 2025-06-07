package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 菜品相关接口
 */
@Slf4j
@RestController
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping
    public Result<String> addDish(@RequestBody DishDTO dishDTO) {
        log.info("添加菜品，{}", dishDTO);
        dishService.addDish(dishDTO);
        return Result.success();
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO 菜品分页查询数据分封装
     * @return 返回给前端的结果
     */
    @GetMapping("/page")
    public Result<PageResult> getDishByPage(DishPageQueryDTO dishPageQueryDTO) {
        log.info("分页查询菜品信息，{}", dishPageQueryDTO);
        return Result.success(dishService.getDishByPage(dishPageQueryDTO));
    }


}
