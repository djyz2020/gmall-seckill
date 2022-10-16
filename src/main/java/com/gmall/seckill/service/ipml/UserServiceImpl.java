package com.gmall.seckill.service.ipml;

import com.gmall.seckill.dao.UserMapper;
import com.gmall.seckill.po.User;
import com.gmall.seckill.dto.LoginParam;
import com.gmall.seckill.result.CodeMsg;
import com.gmall.seckill.result.Result;
import com.gmall.seckill.service.UserService;
import com.gmall.seckill.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserMapper userMapper;
    @Override
    public Result<User> login(LoginParam loginParam) {
        User user = userMapper.checkPhone(loginParam.getMobile());
        if(user == null){
            return Result.error(CodeMsg.MOBILE_NOT_EXIST);
        }
        String dbPwd= user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(loginParam.getPassword(), saltDB);
        if(!StringUtils.equals(dbPwd , calcPass)){
            return Result.error(CodeMsg.PASSWORD_ERROR);
        }
        user.setPassword(StringUtils.EMPTY);
        return Result.success(user);
    }
}
