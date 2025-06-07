package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDish(DishDTO dishDTO) {
        //构造菜品信息对象
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dish.setStatus(StatusConstant.DISABLE);

        //向数据库添加菜品信息
        dishMapper.addDish(dish);

        //获取菜品口味信息
        List<DishFlavor> dishFlavors = dishDTO.getFlavors();
        if (dishFlavors != null && !dishFlavors.isEmpty()) {

            //获取菜品ID
            Long dishId = dish.getId();

            //为菜品口味设置对应的归属菜品ID
            for (DishFlavor df : dishFlavors) {
                df.setDishId(dishId);
            }

            //向数据库中添加菜品口味信息
            dishFlavorMapper.addDishFlavors(dishFlavors);
        }
    }


    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO 菜品分页查询数据封装
     * @return 分页查询结果
     */
    @Override
    public PageResult getDishByPage(DishPageQueryDTO dishPageQueryDTO) {
        int page = dishPageQueryDTO.getPage() == 0 ? 1 : dishPageQueryDTO.getPage();
        int pageSize = dishPageQueryDTO.getPageSize() == 0 ? 10 : dishPageQueryDTO.getPageSize();

        PageHelper.startPage(page, pageSize);
        List<DishVO> dishList = dishMapper.getDishByPage(dishPageQueryDTO);

        //将dishList强制转换为PageInfo以获得目标信息
        PageInfo<DishVO> pageInfo = new PageInfo<>(dishList);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }
}
