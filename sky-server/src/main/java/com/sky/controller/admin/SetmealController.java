package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 增加套餐
     * @param setmealDTO 套餐信息封装
     * @return 正常添加结果
     */
    @PostMapping
    public Result<String> addSetmeal(@RequestBody SetmealDTO setmealDTO){
        log.info("增加套餐，{}", setmealDTO);
        setmealService.addSetmeal(setmealDTO);
        return Result.success();
    }

    /**
     * 批量删除套餐
     * @param ids 套餐ID集合
     * @return 正常删除结果
     */
    @DeleteMapping
    public Result<String> deleteSetmeal(@RequestParam List<String> ids){
        log.info("批量删除套餐，套餐id={}",ids);
        setmealService.deleteSetmeal(ids);
        return Result.success();
    }

    /**
     * 根据ID查询套餐信息
     * @param id 套餐ID
     * @return 套餐信息
     */
    @GetMapping("/{id}")
    public Result<SetmealVO> getSetmealById(@PathVariable String id){
        log.info("根据ID查询套餐，id={}",id);
        return Result.success(setmealService.getSetmealById(id));
    }

}
