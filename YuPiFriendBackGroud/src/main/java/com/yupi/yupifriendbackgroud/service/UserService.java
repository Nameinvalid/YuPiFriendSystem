package com.yupi.yupifriendbackgroud.service;

import com.yupi.yupifriendbackgroud.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 *
 * @author 张家维
 */
public interface UserService extends IService<User> {

    /**
     * 用户校验
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount,String userPassword,String checkPassword);


    /**
     * 用户登录
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @return 脱敏之后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUser 原始用户信息
     * @return 脱敏之后的用户信息
     */
    User safetyUser(User originUser);

}
