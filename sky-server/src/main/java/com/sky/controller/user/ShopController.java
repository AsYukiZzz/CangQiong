package com.sky.controller.user;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺状态管理
 */
@Slf4j
@RestController("userShopController")
@RequestMapping("/user/shop")
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 用户端获取店铺经营状态
     * @return 店铺状态
     */
    @GetMapping("/status")
    public Result<Integer> getShopStatus() {
        log.info("用户端获取店铺状态");
        Integer status = Integer.parseInt((String) redisTemplate.opsForValue().get("shop_status"));
        return Result.success(status);
    }
}
