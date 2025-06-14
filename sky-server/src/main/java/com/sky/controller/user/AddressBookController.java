package com.sky.controller.user;

import com.sky.context.CurrentHolderInfo;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 查询当前登录用户的所有地址信息
     *
     * @return 地址信息集合封装
     */
    @GetMapping("/list")
    public Result<List<AddressBook>> list() {
        Long userId = CurrentHolderInfo.getCurrentHolder();
        log.info("查询用户userID={}所有地址", userId);
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(userId);
        List<AddressBook> list = addressBookService.list(addressBook);
        return Result.success(list);
    }

    /**
     * 新增地址
     *
     * @param addressBook 地址信息封装
     * @return 新增成功结果返回
     */
    @PostMapping
    public Result<String> save(@RequestBody AddressBook addressBook) {
        log.info("用户新增地址，{}", addressBook);
        addressBookService.save(addressBook);
        return Result.success();
    }

    /**
     * 根据地址ID查询地址信息
     *
     * @param id 地址ID
     * @return 地址信息封装
     */
    @GetMapping("/{id}")
    public Result<AddressBook> getById(@PathVariable Long id) {
        log.info("根据地址ID查询地址，id={}", id);
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * 根据地址ID修改地址信息
     *
     * @param addressBook 更改后地址信息封装
     * @return 修改成功结果返回
     */
    @PutMapping
    public Result<String> update(@RequestBody AddressBook addressBook) {
        log.info("根据地址ID修改地址信息，{}", addressBook);
        addressBookService.update(addressBook);
        return Result.success();
    }

    /**
     * 设置默认地址
     *
     * @param addressBook 默认地址特征封装
     * @return 设置成功结果返回
     */
    @PutMapping("/default")
    public Result<String> setDefault(@RequestBody AddressBook addressBook) {
        log.info("用户userId={}，设置默认地址：{}", CurrentHolderInfo.getCurrentHolder(), addressBook);
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    /**
     * 根据地址ID删除地址
     *
     * @param id 地址ID
     * @return 删除成功结果返回
     */
    @DeleteMapping
    public Result<String> deleteById(Long id) {
        log.info("根据ID地址删除地址，id={}", id);
        addressBookService.deleteById(id);
        return Result.success();
    }

    /**
     * 查询用户默认地址
     */
    @GetMapping("/default")
    public Result<AddressBook> getDefault() {

        log.info("查询用户默认地址，userId={}", CurrentHolderInfo.getCurrentHolder());
        AddressBook addressBook = new AddressBook();
        addressBook.setIsDefault(1);
        addressBook.setUserId(CurrentHolderInfo.getCurrentHolder());
        List<AddressBook> list = addressBookService.list(addressBook);

        if (list != null && list.size() == 1) {
            return Result.success(list.get(0));
        }

        return Result.error("没有查询到默认地址");
    }

}
