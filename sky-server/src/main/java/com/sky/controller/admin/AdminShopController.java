package com.sky.controller.admin;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * 店铺状态管理
 */
@Slf4j
@RestController
@RequestMapping("/admin/shop")
public class AdminShopController {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 管理端获取店铺经营状态
     *
     * @return 店铺状态
     */
    @GetMapping("/status")
    public Result<Integer> getShopStatus() {
        log.info("管理端获取店铺营业状态");
        Integer status = Integer.valueOf((String) Objects.requireNonNull(redisTemplate.opsForValue().get("shop_status")));
        return Result.success(status);
    }

    /**
     * 管理端设置店铺经营状态
     *
     * @param status 店铺状态
     * @return 成功操作结果
     */
    @PutMapping("/{status}")
    public Result<String> updateShopStatus(@PathVariable("status") String status) {
        log.info("管理端更改店铺营业状态，修改后营业状态status={}", status);
        redisTemplate.opsForValue().set("shop_status", status);
        return Result.success();
    }
}
