package com.yupi.yupifriendbackgroud.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yupi.yupifriendbackgroud.common.BaseResponse;
import com.yupi.yupifriendbackgroud.common.ErrorCode;
import com.yupi.yupifriendbackgroud.common.ResultUtils;
import com.yupi.yupifriendbackgroud.constant.UserConstant;
import com.yupi.yupifriendbackgroud.exception.BusinessException;
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
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String idNumber=userRegisterRequest.getIdNumber();
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,idNumber)){
            return ResultUtils.error(ErrorCode.NULL_ERROR,ErrorCode.PARAMS_ERROR.getMessage());
        }
        Long result=userService.userRegister(userAccount,userPassword,checkPassword,idNumber);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录接口
     *
     * @param userLoginRequest 用户登录请求体
     * @return 返回用户托名之后的信息
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        User user=userService.userLogin(userAccount,userPassword,request);
        return ResultUtils.success(user);
    }

    /**
     * 用户注销
     * @param httpServletRequest
     * @return 返回用户是否注销成功
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest httpServletRequest){
        if (httpServletRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(httpServletRequest);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前用户，使用session
     *
     * @param request
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE.getResult());
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = currentUser.getId();
        // TODO 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.safetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    /**
     * 用户搜索类
     * @param username 用户名的模糊查询
     * @return 返回所有的用户或者模糊查询的用户
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username,HttpServletRequest request){
        if (!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH, "缺少管理员权限");
        }
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }
        List<User> userList=userService.list(queryWrapper);
        //进行用户脱敏
        List<User> list=userList.stream().map(user -> userService.safetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id,HttpServletRequest request){
        //判断是否是管理员
        if (!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (id<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
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
        //对拿到的登录信息做一个权限的判断，如果是管理员则返回ture
        return user != null && user.getUserRole() == UserConstant.ADMIN_ROLE.getCode();
    }
}
