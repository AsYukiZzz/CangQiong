package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {

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
    void save(AddressBook addressBook);

    /**
     * 根据地址ID查询地址信息
     *
     * @param id 地址ID
     * @return 地址信息封装
     */
    AddressBook getById(Long id);

    /**
     * 根据地址ID修改地址信息
     *
     * @param addressBook 更改后地址信息封装
     */
    void update(AddressBook addressBook);

    /**
     * 设置默认地址
     *
     * @param addressBook 默认地址特征封装
     */
    void setDefault(AddressBook addressBook);

    /**
     * 根据地址ID删除地址
     *
     * @param id 地址ID
     */
    void deleteById(Long id);

}
