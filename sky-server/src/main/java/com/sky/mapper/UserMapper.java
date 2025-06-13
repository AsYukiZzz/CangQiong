package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    /**
     * 根据openId获取用户信息
     * @param openId 微信登录唯一标识ID
     * @return 用户信息
     */
    @Select("select id,openid,name,phone,sex,id_number,avatar,create_time from user where openid = #{openId}")
    User getUserByOpenId(@Param("openId") String openId);

    /**
     * 保存用户信息到数据库
     * @param user 用户信息
     */
    void saveUserInfo(User user);

    /**
     * 根据ID查询用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    @Select("select * from user where id = #{id}")
    User getUserById(@Param("id") String id);
}
