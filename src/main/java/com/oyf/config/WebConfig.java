package com.oyf.config;

import com.oyf.filter.AclFilter;
import com.oyf.filter.LoginFilter;
import com.oyf.interceptor.HttpInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Create Time: 2019年04月28日 15:34
 * Create Author: 欧阳飞
 **/

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /*配置拦截器*/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HttpInterceptor()).addPathPatterns("/sys/");
    }

    /*配置登录过滤器*/
    @Bean
    public FilterRegistrationBean loginFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new LoginFilter());
        registration.addUrlPatterns("/*"); //
        registration.addInitParameter("paramName", "paramValue"); //
        registration.setName("loginFilter");
        registration.setOrder(1);
        return registration;
    }

    /*配置权限过滤器*/
    @Bean
    public FilterRegistrationBean aclFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new AclFilter());
        registration.addUrlPatterns("/*"); //
        registration.addInitParameter("paramName", "paramValue"); //
        registration.setName("aclFilter");
        registration.setOrder(2);
        return registration;

    }
}
