package com.gmall.seckill.controller;

import com.gmall.seckill.common.RedisConst;
import com.gmall.seckill.po.User;
import com.gmall.seckill.dto.LoginParam;
import com.gmall.seckill.redis.RedisService;
import com.gmall.seckill.redis.UserKey;
import com.gmall.seckill.common.CommonResult;
import com.gmall.seckill.service.UserService;
import com.gmall.seckill.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    @Resource
    private RedisService redisService;

    @Resource
    private UserService userService;

    @RequestMapping(value = {"/", "/index"})
    public ModelAndView index(ModelAndView modelAndView){
        Map<String, String> attrs = new HashMap<>();
        modelAndView.addObject(attrs);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping("/user/login")
    @ResponseBody
    public CommonResult<User> doLogin(HttpServletResponse response, HttpSession session, @Valid LoginParam loginParam) {
        CommonResult<User> login = userService.login(loginParam);
        if (login.isSuccess()){
            CookieUtil.writeLoginToken(response,session.getId());
            redisService.set(UserKey.getByName , session.getId() ,login.getData(), RedisConst.RedisCacheExtime.REDIS_SESSION_EXTIME);
        }
        return login;
    }

    @RequestMapping("/user/logout")
    public String doLogout(HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtil.readLoginToken(request);
        CookieUtil.delLoginToken(request , response);
        redisService.del(UserKey.getByName , token);
        return "index";
    }
}
