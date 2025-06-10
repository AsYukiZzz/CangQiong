package com.sky.controller.user;

import com.sky.dto.UserLoginDTO;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.LoginException;

/**
 * 用户管理
 */
@Slf4j
@RestController
@RequestMapping("/user/user")
public class UserController {


    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param userLoginDTO 用户登录数据对象封装
     * @return 用户登陆成功返回数据封装
     */
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) throws LoginException {
        log.info("用户端发起登录请求，唯一标识code={}", userLoginDTO.getCode());
        return Result.success(userService.userLogin(userLoginDTO));
    }
}
