package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface AddressBookMapper {

    /**
     * 查询符合条件的所有地址信息
     *
     * @param addressBook 条件信息封装
     * @return 地址信息集合封装
     */
    List<AddressBook> list(AddressBook addressBook);

    /**
     * 新增地址
     *
     * @param addressBook 地址信息封装
     */
    @Insert("insert into address_book" +
            "        (user_id, consignee, phone, sex, province_code, province_name, city_code, city_name, district_code," +
            "         district_name, detail, label, is_default)" +
            "        values (#{userId}, #{consignee}, #{phone}, #{sex}, #{provinceCode}, #{provinceName}, #{cityCode}, #{cityName}," +
            "                #{districtCode}, #{districtName}, #{detail}, #{label}, #{isDefault})")
    void insert(AddressBook addressBook);

    /**
     * 根据地址ID查询地址信息
     *
     * @param id 地址ID
     * @return 地址信息封装
     */
    @Select("select * from address_book where id = #{id}")
    AddressBook getById(Long id);

    /**
     * 根据地址ID修改地址信息
     *
     * @param addressBook 更改后地址信息封装
     */
    void update(AddressBook addressBook);

    /**
     * 根据 用户ID修改 是否默认地址
     * @param addressBook 更改地址信息封装
     */
    @Update("update address_book set is_default = #{isDefault} where user_id = #{userId}")
    void updateIsDefaultByUserId(AddressBook addressBook);

    /**
     * 根据地址ID删除地址
     *
     * @param id 地址ID
     */
    @Delete("delete from address_book where id = #{id}")
    void deleteById(Long id);

}
