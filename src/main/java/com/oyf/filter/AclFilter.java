package com.oyf.filter;

import com.oyf.model.SysUser;
import com.oyf.tree.SysCoreService;
import com.oyf.utils.ApplicationContextHelper;
import com.oyf.utils.JsonData;
import com.oyf.utils.JsonMapper;
import com.oyf.utils.LoginHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Create Time: 2019年03月29日 16:19
 * Create Author: 欧阳飞
 **/

public class AclFilter implements Filter {

    private static final String noAuthUrl = "/sys/user/noAuth.page";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();

        if (uri.contains("signin") || uri.contains("login") || uri.contains(noAuthUrl)){
            filterChain.doFilter(request,response);
            return;
        }
        SysUser sysUser = LoginHolder.getUserHolder();
        if (sysUser == null){
            //用户没有登录的情况下
            noAuth(request,response);
            return;
        }

        SysCoreService sysCoreService = ApplicationContextHelper.popBean(SysCoreService.class);
        //如果没有权限
        if ( !sysCoreService.hasAcl(uri)){
            noAuth(request,response);
            return;
        }
        //其它情况放行
        filterChain.doFilter(request,response);
    }

    //没有权限的提示
    private void noAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String uri = request.getRequestURI();
        if (uri.endsWith(".json")){//json请求
            JsonData jsonData = JsonData.fail("没有权限，若需要请联系管理员");
            response.setHeader("Content-Type","application/json");
            response.getWriter().print(JsonMapper.obj2String(jsonData));
        }else {//page请求
            response.setHeader("Content-Type","text/html");
            response.getWriter().print("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
                    + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + "<head>\n" + "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>\n"
                    + "<title>跳转中...</title>\n" + "</head>\n" + "<body>\n" + "跳转中，请稍候...\n" + "<script type=\"text/javascript\">//<![CDATA[\n"
                    + "window.location.href='" + noAuthUrl + "?ret='+encodeURIComponent(window.location.href);\n" + "//]]></script>\n" + "</body>\n" + "</html>\n");
        }
    }

    @Override
    public void destroy() {

    }
}
