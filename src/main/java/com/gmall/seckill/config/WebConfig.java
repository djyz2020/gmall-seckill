package com.gmall.seckill.config;

import com.gmall.seckill.filter.SessionValidFilter;
import com.gmall.seckill.interceptor.AuthorizationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.servlet.Filter;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    @Autowired
    AuthorizationInterceptor authorizationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        // 映射为 user 的控制器下的所有映射
        //registry.addInterceptor(authorizationInterceptor).addPathPatterns("/user/login").excludePathPatterns("/index", "/");
        registry.addInterceptor(authorizationInterceptor);
        super.addInterceptors(registry);
    }

    @Bean
    public FilterRegistrationBean uploadFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new DelegatingFilterProxy("sessionValidFilter"));
        registration.addUrlPatterns("/**");
        registration.setName("sessionValidFilter");
        registration.setOrder(1);
        return registration;
    }


}
