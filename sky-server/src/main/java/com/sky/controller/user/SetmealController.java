package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户端套餐相关接口
 */
@Slf4j
@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 根据分类ID查询套餐列表
     *
     * @param setmealDTO 前端查询条件封装
     * @return 套餐列表信息封装
     */
    @GetMapping("/list")
    public Result<List<Setmeal>> list(SetmealDTO setmealDTO) {
        log.info("用户端根据分类ID查询套餐信息，categoryId={}",setmealDTO.getCategoryId());
        setmealDTO.setStatus(StatusConstant.ENABLE);
        List<Setmeal> list = setmealService.list(setmealDTO);
        return Result.success(list);
    }

    /**
     * 根据套餐ID查询菜品列表
     *
     * @param id 套餐ID
     * @return 菜品列表信息封装
     */
    @GetMapping("/dish/{id}")
    public Result<List<DishItemVO>> dishList(@PathVariable("id") Long id) {
        List<DishItemVO> list = setmealService.getDishItemById(id);
        return Result.success(list);
    }
}
