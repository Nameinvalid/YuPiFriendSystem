package com.yupi.yupifriendbackgroud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yupifriendbackgroud.model.domain.User;
import com.yupi.yupifriendbackgroud.service.UserService;
import com.yupi.yupifriendbackgroud.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lenovo
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2023-09-26 20:47:12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {


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
        final String SALT="zhangjiawei";
        String saltPassword=DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
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
}




