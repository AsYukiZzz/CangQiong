package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 批量删除菜品
     * @param ids 待删除菜品ID
     * @return 成功返回结果
     */
    @DeleteMapping
    public Result<String> deleteDishes(@RequestParam List<String> ids) {
        log.info("批量删除菜品，id={}", ids);
        dishService.deleteDishes(ids);
        return Result.success();
    }

    /**
     * 根据ID查询菜品
     * @param id 待查询菜品ID
     * @return 含有菜品的结果封装信息
     */
    //TODO 错误：菜品口味信息应一并返回到前端
    @GetMapping("/{id}")
    public Result<DishVO> getDishById(@PathVariable String id) {
        log.info("根据ID查询菜品，id={}",id);
        return Result.success(dishService.getDishById(id));
    }

    /**
     * 根据ID更改菜品启用状态
     * @param status 状态值
     * @param id 菜品ID
     * @return 成功结果返回
     */
    @PostMapping("/status/{status}")
    public Result<String> updateDishStatus(@PathVariable String status, @RequestParam Long id) {
        log.info("修改菜品启用状态，菜品ID={}，修改后状态status={}",id,status);
        dishService.updateDishStatus(status,id);
        return Result.success();
    }

    @PutMapping
    public Result<String> updateDish(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品信息，{}", dishDTO);
        dishService.updateDish(dishDTO);
        return Result.success();
    }
}
