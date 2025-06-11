package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.CacheNameConstant;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;


@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 增加菜品
     *
     * @param dishDTO 菜品信息封装
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = CacheNameConstant.CATEGORY_DISH, key = "#dishDTO.categoryId")
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

    /**
     * 批量删除菜品
     *
     * @param ids 待删除菜品ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = CacheNameConstant.CATEGORY_DISH, allEntries = true)
    public void deleteDishes(List<String> ids) {
        DishVO dish;
        //检查菜品状态
        for (String id : ids) {
            dish = dishMapper.getDishById(id);
            if (Objects.equals(dish.getStatus(), StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        //检查菜品是否被套餐关联：被关联则无法删除
        List<SetmealDish> setmealDishList = setmealDishMapper.getSetmealDishByDishId(ids);
        if (setmealDishList != null && !setmealDishList.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //删除菜品
        dishMapper.deleteDishByIds(ids);

        //删除菜品口味
        dishFlavorMapper.deleteDishFlavorByIds(ids);
    }

    /**
     * 根据ID查询菜品
     *
     * @param id 待查询菜品ID
     * @return 菜品信息
     */
    @Override
    public DishVO getDishById(String id) {
        //获取菜品信息
        DishVO dishVO = dishMapper.getDishById(id);

        if (dishVO != null) {
            dishVO.setFlavors(dishFlavorMapper.getDishFlavorById(id));
        }
        //获取菜品口味信息
        return dishVO;
    }

    /**
     * 根据ID更改菜品启用状态
     *
     * @param dishDTO 待更改信息封装
     */
    @Override
    @CacheEvict(cacheNames = CacheNameConstant.CATEGORY_DISH, key = "#dishDTO.categoryId")
    public void updateDishStatus(DishDTO dishDTO) {
        Dish dish = Dish.builder()
                .id(dishDTO.getId())
                .status(dishDTO.getStatus())
                .build();

        //更新菜品状态
        dishMapper.updateDish(dish);

        //将菜品的categoryId返回给dishDTO对象，以完成缓存相关操作
        dishDTO.setCategoryId(dish.getCategoryId());
    }

    /**
     * 根据ID修改菜品信息
     *
     * @param dishDTO 待更改信息封装
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = CacheNameConstant.CATEGORY_DISH, key = "#dishDTO.categoryId")
    public void updateDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        //更新菜品信息
        dishMapper.updateDish(dish);

        Long id = dish.getId();

        //删除原有菜品口味信息
        dishFlavorMapper.deleteDishFlavorByIds(Collections.singletonList(String.valueOf(id)));

        //增加修改后的菜品口味信息
        if (dishDTO.getFlavors() != null && !dishDTO.getFlavors().isEmpty()) {
            for (DishFlavor df : dishDTO.getFlavors()) {
                df.setDishId(id);
            }
            dishFlavorMapper.addDishFlavors(dishDTO.getFlavors());
        }
    }

    @Override
    public List<DishVO> getDishByList(String categoryId) {
        DishDTO dishDTO = DishDTO.builder().categoryId(Long.valueOf(categoryId)).build();
        return dishMapper.getDishByList(dishDTO);
    }

    /**
     * 条件查询菜品和口味
     *
     * @param dishDTO 查询条件封装
     * @return 符合条件的菜品信息集合封装
     */
    @Cacheable(cacheNames = CacheNameConstant.CATEGORY_DISH, key = "#dishDTO.categoryId")
    public List<DishVO> listWithFlavor(DishDTO dishDTO) {
        List<DishVO> dishList = dishMapper.getDishByList(dishDTO);

        for (DishVO d : dishList) {
            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getDishFlavorById(String.valueOf(d.getId()));
            d.setFlavors(flavors);
        }

        return dishList;
    }
}
