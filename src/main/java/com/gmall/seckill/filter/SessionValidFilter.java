package com.gmall.seckill.filter;

import com.gmall.seckill.common.RedisConst;
import com.gmall.seckill.po.User;
import com.gmall.seckill.cache.RedisService;
import com.gmall.seckill.cache.UserKey;
import com.gmall.seckill.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 重新设置用户session在redis的有效期
 */
@Component
@Order(1)
public class SessionValidFilter implements Filter {

    @Resource
    private RedisService redisService;

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);

        if(StringUtils.isNotEmpty(loginToken)){
            //判断logintoken是否为空或者""；
            //如果不为空的话，符合条件，继续拿user信息
            User user = redisService.get(UserKey.getByName,loginToken, User.class);
            if(user != null){
                //如果user不为空，则重置session的时间，即调用expire命令
                redisService.expire(UserKey.getByName , loginToken, RedisConst.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {}
}
