package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.mapper.UserMapper;
import com.sky.properties.JwtProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     * @param userLoginDTO 用户登录数据封装
     * @return 用户登录成功返回数据封装
     * @throws LoginException 登录失败异常
     */
    @Override
    public UserLoginVO userLogin(UserLoginDTO userLoginDTO) throws LoginException {
        //从系统环境变量获取appid与secret
        String appid = System.getenv("WECHAT_APPID");
        String secret = System.getenv("WECHAT_SECRET");

        //封装请求参数列表，并通过微信auth.code2session接口获取code对应的OpenID
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", appid);
        paramMap.put("secret", secret);
        paramMap.put("js_code", userLoginDTO.getCode());
        paramMap.put("grand_type", "authorization_code");

        String result = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/jscode2session", paramMap);

        //解析返回的json结果，获取openId
        JSONObject jsonObject = JSONObject.parseObject(result);
        String openId = jsonObject.getString("openid");
        if (openId == null) {
            throw new LoginException(MessageConstant.LOGIN_FAILED);
        }

        //根据openId搜索用户是否存在
        User user = userMapper.getUserByOpenId(openId);

        //若数据库中不存在该用户信息，则保存该用户信息
        if (user == null) {
            user = User.builder()
                    .openid(openId)
                    .createTime(LocalDateTime.now())
                    .build();

            userMapper.saveUserInfo(user);
        }

        //构造JWT负载列表
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());

        //构造Token
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);

        Long userId = user.getId();
        log.info("用户Token构造完成，id={}，token={}", userId, token);

        //返回构造结果
        return UserLoginVO.builder()
                .openid(openId)
                .id(userId)
                .token(token)
                .build();
    }
}
