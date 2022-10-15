package com.gmall.seckill.service;

import com.gmall.seckill.po.User;
import com.gmall.seckill.dto.LoginParam;
import com.gmall.seckill.result.Result;

public interface UserService {

    Result<User> login(LoginParam loginParam);

}
