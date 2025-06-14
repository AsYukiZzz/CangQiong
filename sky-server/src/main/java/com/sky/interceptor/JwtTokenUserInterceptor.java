package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.CurrentHolderInfo;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户端登录校验拦截器
 */
@Slf4j
@Component
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 用户端请求拦截：JWT校验
     *
     * @param request  请求体对象
     * @param response 响应体对象
     * @param handler  被拦截对象
     * @return 布尔值，为true时放行请求
     * @throws Exception 可能抛出的异常
     */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //从请求头获取Token
        String token = request.getHeader(jwtProperties.getUserTokenName());

        //校验令牌
        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            log.info("当前用户id：{}", userId);
            CurrentHolderInfo.setCurrentHolder(userId, CurrentHolderInfo.USER_SIDE);
            //3、通过，放行
            return true;
        } catch (Exception ex) {
            //4、不通过，响应401状态码
            response.setStatus(401);
            return false;
        }
    }

    /**
     * 移除ThreadLocal，避免内存泄漏
     *
     * @param request  请求体对象
     * @param response 响应体对象
     * @param handler  被拦截内容
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        CurrentHolderInfo.removeCurrentHolder();
    }
}
