package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜品相关接口
 */
@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     * 根据分类ID查询菜品
     *
     * @param dishDTO 查询条件封装
     * @return 菜品信息封装
     */
    @GetMapping("/list")
    public Result<List<DishVO>> list(DishDTO dishDTO) {
        log.info("根据分类ID查询菜品信息，分类ID={}", dishDTO.getCategoryId());

        //设置查询条件为起售状态下的菜品
        dishDTO.setStatus(StatusConstant.ENABLE);//查询起售中的菜品
        List<DishVO> list = dishService.listWithFlavor(dishDTO);

        return Result.success(list);
    }

}
