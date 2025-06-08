package com.sky.mapper;

import com.sky.anno.Autofill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id 分类ID
     * @return 套餐数量
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO 套餐分页查询参数
     * @return 套餐分页查询结果
     */
    List<Setmeal> getSetmealByPage(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 添加套餐
     * @param setmeal 套餐信息封装
     */
    @Autofill(OperationType.INSERT)
    void addSetmeal(Setmeal setmeal);

    /**
     * 根据ID查询套餐信息
     * @param id 套餐ID
     * @return 套餐信息封装
     */
    @Select("select s.id, s.category_id, s.name, s.price, s.status, s.description, s.image, s.update_user, c.name as categoryName from setmeal s left join category c on s.category_id = c.id where s.id=#{id}")
    SetmealVO getSetmealById(@Param("id") String id);

    /**
     * 批量删除套餐
     * @param ids 套餐ID集合
     */
    void deleteSetmeals(List<String> ids);

    /**
     * 根据ID更改套餐信息
     * @param setmeal 套餐信息封装
     */
    @Autofill(OperationType.UPDATE)
    void updateSetmeal(Setmeal setmeal);
}
