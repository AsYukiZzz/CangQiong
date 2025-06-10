package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.vo.UserLoginVO;

import javax.security.auth.login.LoginException;

public interface UserService {

    /**
     * 用户登录
     * @param userLoginDTO 用户登录数据封装
     * @return 用户登录成功返回数据封装
     * @throws LoginException 登录失败异常
     */
    UserLoginVO userLogin(UserLoginDTO userLoginDTO) throws LoginException;
}
