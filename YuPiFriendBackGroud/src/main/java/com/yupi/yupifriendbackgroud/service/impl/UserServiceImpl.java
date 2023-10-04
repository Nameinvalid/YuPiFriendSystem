package com.yupi.yupifriendbackgroud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yupifriendbackgroud.constant.UserConstant;
import com.yupi.yupifriendbackgroud.model.domain.User;
import com.yupi.yupifriendbackgroud.service.UserService;
import com.yupi.yupifriendbackgroud.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lenovo
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2023-09-26 20:47:12
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    UserMapper userMapper;


    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1. 校验 使用StringUtils.isAnyBlank可以直接一组进行校验不为空，不需要写||
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1;
        }
        if (userAccount.length() < 4) {
            return -2;
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            return -3;
        }
        //账户里不能包含特殊字符；
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Matcher matcher = Pattern.compile(regEx).matcher(userAccount);
        if (matcher.find()) {
            return -4;
        }
        //验证密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            return -5;
        }
        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();//这个可以判定是否有相同的
        queryWrapper.eq("user_account", userAccount);
        long count = this.count(queryWrapper);
        if (count > 0) {
            return -6;
        }
        //2. 对密码进行加密
        String saltPassword=DigestUtils.md5DigestAsHex((UserConstant.SALT.getResult()+userPassword).getBytes());
        //3.向数据库里面加入数据
        User user=new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(saltPassword);

        //boolean saveResult=this.save(user);//this=userMapper
        boolean saveResult=this.save(user);
        if (!saveResult){
            return -1;
        }

        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1. 校验 使用StringUtils.isAnyBlank可以直接一组进行校验不为空，不需要写||
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            // todo 统一改成异常
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (userPassword.length() < 8 ) {
            return null;
        }
        //账户里不能包含特殊字符；
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Matcher matcher = Pattern.compile(regEx).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }
        //2. 对密码进行加密
        String saltPassword=DigestUtils.md5DigestAsHex((UserConstant.SALT.getResult()+userPassword).getBytes());
        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();//这个可以判定是否有相同的
        queryWrapper.eq("user_account", userAccount);
        queryWrapper.eq("user_password",userPassword);
        User user=userMapper.selectOne(queryWrapper);
        // 如果用户不存在返回null
        if (user==null){
            log.info("user login failed,userAccount can not match userPassword");
            return null;
        }
        //3. 用户脱敏
        User safetyUser=safetyUser(user);
        // 4.记录用户登录态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE.getResult(),safetyUser);
        return safetyUser;
    }

    @Override
    public User safetyUser(User originUser) {
        User safetyUser=new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setTags(originUser.getTags());
        return safetyUser;
    }

    /**
     * 用户注销
     * @param request
     * @return
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        //移除登录态
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE.getResult());
        return 1;
    }


}




