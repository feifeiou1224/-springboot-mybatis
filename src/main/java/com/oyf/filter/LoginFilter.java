package com.oyf.filter;

import com.oyf.model.SysUser;
import com.oyf.utils.LoginHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Create Time: 2019年03月22日 17:10
 * Create Author: 欧阳飞
 **/


/*
*   作用：未登录时无法访问登录外的页面
*           登录成功时将登录的user对象和ip存进threadLocal
*
*
* */

public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri = request.getRequestURI();

        if (uri.contains("login")||uri.contains("signin")||uri.contains("/js/")||uri.contains("/css/")||uri.contains("/bootstrap3.3.5/")
                ||uri.contains("/assets/")||uri.contains("/mustache/")||uri.contains("/ztree/")){//如果是以上路径 放行
            filterChain.doFilter(request,response);
        }else {//如果不是以上路径
            SysUser sysUser = (SysUser) request.getSession().getAttribute("user");
            if (sysUser == null){//没有登录
                response.sendRedirect("/signin.jsp");
                return;
            }else {//登录了
                //绑定user和request
                LoginHolder.setHolder(sysUser,request);
                filterChain.doFilter(request,response);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
