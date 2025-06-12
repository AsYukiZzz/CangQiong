package com.sky.service.impl;

import com.sky.context.CurrentHolder;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class AddressBookServiceImpl implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 查询符合条件的所有地址信息
     *
     * @param addressBook 条件信息封装
     * @return 地址信息集合封装
     */
    public List<AddressBook> list(AddressBook addressBook) {
        return addressBookMapper.list(addressBook);
    }

    /**
     * 新增地址
     *
     * @param addressBook 地址信息封装
     */
    public void save(AddressBook addressBook) {
        addressBook.setUserId(CurrentHolder.getCurrentHolder());
        addressBook.setIsDefault(0);
        addressBookMapper.insert(addressBook);
    }

    /**
     * 根据地址ID查询地址信息
     *
     * @param id 地址ID
     * @return 地址信息封装
     */
    public AddressBook getById(Long id) {
        return addressBookMapper.getById(id);
    }

    /**
     * 根据地址ID修改地址信息
     *
     * @param addressBook 更改后地址信息封装
     */
    public void update(AddressBook addressBook) {
        addressBookMapper.update(addressBook);
    }

    /**
     * 设置默认地址
     *
     * @param addressBook 默认地址特征封装
     */
    @Transactional
    public void setDefault(AddressBook addressBook) {
        //1、将当前用户的所有地址修改为非默认地址 update address_book set is_default = ? where user_id = ?
        addressBook.setIsDefault(0);
        addressBook.setUserId(CurrentHolder.getCurrentHolder());
        addressBookMapper.updateIsDefaultByUserId(addressBook);

        //2、将当前地址改为默认地址 update address_book set is_default = ? where id = ?
        addressBook.setIsDefault(1);
        addressBookMapper.update(addressBook);
    }

    /**
     * 根据地址ID删除地址
     *
     * @param id 地址ID
     */
    public void deleteById(Long id) {
        addressBookMapper.deleteById(id);
    }

}
