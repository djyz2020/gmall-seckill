package com.gmall.seckill.service;

import com.gmall.seckill.po.User;
import com.gmall.seckill.dto.LoginParam;
import com.gmall.seckill.common.CommonResult;

public interface UserService {

    CommonResult<User> login(LoginParam loginParam);

}
