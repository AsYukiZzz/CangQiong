package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO 套餐分页查询参数封装
     * @return 分页查询结果
     */
    @Override
    public PageResult getSetmealByPage(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        List<Setmeal> setmealList = setmealMapper.getSetmealByPage(setmealPageQueryDTO);

        //转换为pageInfo对象以获取记录数和记录
        PageInfo<Setmeal> pageInfo = new PageInfo<>(setmealList);
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     * 添加套餐
     * @param setmealDTO 套餐信息封装
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);

        //添加套餐信息
        setmealMapper.addSetmeal(setmeal);

        //获取添加的套餐ID
        Long setmealId = setmeal.getId();

        //获取套餐菜品关系
        List<SetmealDish> setmealDishList = setmealDTO.getSetmealDishes();
        if(setmealDishList !=null && !setmealDishList.isEmpty()){
            for (SetmealDish s : setmealDishList) {
                s.setSetmealId(setmealId);
            }
            //添加套餐-菜品关系信息
            setmealDishMapper.addSetmealDish(setmealDishList);
        }
    }

    /**
     * 批量删除套餐
     * @param ids 套餐ID集合
     */
    @Override
    public void deleteSetmeal(List<String> ids) {
        //检查套餐启用状态：启用的套餐不能删除
        for (String id : ids) {
            SetmealVO setmealVO = setmealMapper.getSetmealById(id);
            if(setmealVO != null && setmealVO.getStatus() == 1){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }

        //删除套餐信息
        setmealMapper.deleteSetmeals(ids);
    }

    /**
     * 根据ID查询套餐信息
     * @param id 套餐ID
     * @return 套餐信息
     */
    @Override
    public SetmealVO getSetmealById(String id) {
        //获取套餐信息
        SetmealVO setmealVO = setmealMapper.getSetmealById(id);

        //获取套餐附属菜品信息
        setmealVO.setSetmealDishes(setmealDishMapper.getSetmealDishBySetmealId(id));

        return setmealVO;
    }

    /**
     * 根据ID更改套餐信息
     * @param setmealDTO 套餐信息封装
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSetmeal(SetmealDTO setmealDTO) {
        //更改套餐信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.updateSetmeal(setmeal);

        //获取套餐ID
        Long setmealId = setmeal.getId();

        //删除套餐-菜品关系信息
        setmealDishMapper.deleteSetmealDishBySetmealId(setmealId);

        //添加更改后的套餐-菜品关系信息
        for (SetmealDish setmealDish : setmealDTO.getSetmealDishes()) {
            //添加套餐ID
            setmealDish.setSetmealId(setmealId);
        }
        setmealDishMapper.addSetmealDish(setmealDTO.getSetmealDishes());
    }
}
