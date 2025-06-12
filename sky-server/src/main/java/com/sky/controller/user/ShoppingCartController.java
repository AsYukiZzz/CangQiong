package com.sky.controller.user;

import com.sky.context.CurrentHolder;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车相关接口
 */
@Slf4j
@RestController
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 向购物车添加内容
     *
     * @param shoppingCartDTO 添加内容数据封装
     * @return 添加成功返回结果
     */
    @PostMapping("/add")
    public Result<String> addItem(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("向购物车添加内容，{}", shoppingCartDTO);
        shoppingCartService.addItem(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查询购物车内容
     *
     * @return 购物车内容封装
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> listItems() {
        log.info("用户请求购物车内容，用户id={}", CurrentHolder.getCurrentHolder());
        return Result.success(shoppingCartService.listItem());
    }

    /**
     * 清空购物车
     *
     * @return 清空购物车成功返回结果
     */
    @DeleteMapping("/clean")
    public Result<String> ClearItem() {
        log.info("用户清空购物车内容，用户id={}", CurrentHolder.getCurrentHolder());
        shoppingCartService.clearItem();
        return Result.success();
    }

    /**
     * 减少购物车指定物品的数量
     *
     * @param shoppingCartDTO 物品信息封装
     * @return 减少成功返回结果
     */
    @PostMapping("/sub")
    public Result<String> subItem(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("减少购物车的指定物品，{}", shoppingCartDTO);
        shoppingCartService.subItem(shoppingCartDTO);
        return Result.success();
    }
}
