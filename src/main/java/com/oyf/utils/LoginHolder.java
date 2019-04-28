package com.oyf.utils;

import com.oyf.model.SysUser;

import javax.servlet.http.HttpServletRequest;

/**
 * Create Time: 2019年03月22日 19:36
 * Create Author: 欧阳飞
 **/

public class LoginHolder {

    //绑定当前用户
    private static final ThreadLocal<SysUser> userHolder = new ThreadLocal<>();

    //绑定当前request对象
    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();

    public static void setHolder(SysUser sysUser, HttpServletRequest request){
        userHolder.set(sysUser);
        requestHolder.set(request);
    }

    public static SysUser getUserHolder(){
        return userHolder.get();
    }

    public static HttpServletRequest getRequestHolder(){
        return requestHolder.get();
    }

    public static void removeHolder(){
        userHolder.remove();
        requestHolder.remove();
    }
}
