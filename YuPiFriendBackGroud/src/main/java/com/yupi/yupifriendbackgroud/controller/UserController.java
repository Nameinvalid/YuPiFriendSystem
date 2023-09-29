package com.yupi.yupifriendbackgroud.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yupi.yupifriendbackgroud.constant.UserConstant;
import com.yupi.yupifriendbackgroud.model.domain.User;
import com.yupi.yupifriendbackgroud.model.domain.request.UserLoginRequest;
import com.yupi.yupifriendbackgroud.model.domain.request.UserRegisterRequest;
import com.yupi.yupifriendbackgroud.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//@RestController这个注解就是为了让返回前端的所有数据都是json格式
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册校验接口
     *
     * @param userRegisterRequest 用户注册的请求体
     * @return 新用户id
     */
    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest==null){
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            return null;
        }
        return userService.userRegister(userAccount,userPassword,checkPassword);
    }

    /**
     * 用户登录接口
     *
     * @param userLoginRequest 用户登录请求体
     * @return 返回用户托名之后的信息
     */
    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest==null){
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        return userService.userLogin(userAccount,userPassword,request);
    }

    /**
     * 用户搜索类
     * @param username 用户名的模糊查询
     * @return 返回所有的用户或者模糊查询的用户
     */
    @GetMapping("/search")
    public List<User> searchUsers(String username,HttpServletRequest request){
        if (!isAdmin(request)){
            return new ArrayList<>();
        }
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }
        List<User> userList=userService.list(queryWrapper);
        //进行用户脱敏
        return userList.stream().map(user -> userService.safetyUser(user)).collect(Collectors.toList());
    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id,HttpServletRequest request){
        //判断是否是管理员
        if (!isAdmin(request)){
            return false;
        }
        if (id<=0){
            return false;
        }
        return userService.removeById(id);
    }


    /**
     * 是否为管理员
     * @param request 发送的请求
     * @return 返回是否是管理员
     */
    private boolean isAdmin(HttpServletRequest request){
        // 仅管理员可以查询
        // 在request中拿到登录信息
        Object userObj=request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE.getResult());
        User user=(User) userObj;
        //对拿到的登录信息做一个权限的判断，如果不是管理员直接返回一个空数组
        return user == null || user.getUserRole() != UserConstant.ADMIN_ROLE.getCode();
    }
}
