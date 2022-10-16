package com.gmall.seckill.service.impl;

import com.gmall.seckill.common.AppStatus;
import com.gmall.seckill.dao.UserMapper;
import com.gmall.seckill.po.User;
import com.gmall.seckill.dto.LoginParam;
import com.gmall.seckill.common.CommonResult;
import com.gmall.seckill.service.UserService;
import com.gmall.seckill.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService{

    @Resource
    private UserMapper userMapper;

    @Override
    public CommonResult<User> login(LoginParam loginParam) {
        User user = userMapper.checkPhone(loginParam.getMobile());
        if(user == null){
            return CommonResult.error(AppStatus.MOBILE_NOT_EXIST);
        }
        String dbPwd= user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(loginParam.getPassword(), saltDB);
        if(!StringUtils.equals(dbPwd, calcPass)){
            return CommonResult.error(AppStatus.PASSWORD_ERROR);
        }
        user.setPassword(StringUtils.EMPTY);
        return CommonResult.success(user);
    }
}
