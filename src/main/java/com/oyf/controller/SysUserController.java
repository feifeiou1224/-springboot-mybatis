package com.oyf.controller;

import com.oyf.beans.PageBean;
import com.oyf.model.SysUser;
import com.oyf.param.UserLoginParam;
import com.oyf.param.UserParam;
import com.oyf.service.SysUserService;
import com.oyf.utils.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * Create Time: 2019年03月21日 15:48
 * Create Author: 欧阳飞
 **/

@Controller
@RequestMapping("/sys/user")
@Slf4j
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @RequestMapping("/login.page")
    public ModelAndView login(UserLoginParam userLoginParam, HttpServletRequest request){

        log.info("进入controller了");

        SysUser sysUser = sysUserService.findOne(userLoginParam);
        request.getSession().setAttribute("user",sysUser);

        return new ModelAndView("admin");

    }

    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData save(UserParam userParam){

        sysUserService.save(userParam);

        return JsonData.success();

    }

    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData page(Integer deptId, PageBean<SysUser> pageBean){

        PageBean<SysUser> pageBeanById = sysUserService.getPageBeanById(deptId, pageBean);

        return JsonData.success(pageBeanById);

    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData update(UserParam userParam){

        sysUserService.update(userParam);

        return JsonData.success();
    }

    @RequestMapping("/noAuth.page")
    public ModelAndView noAuth(){
        return new ModelAndView("noAuth");
    }

    @RequestMapping("/logout.page")
    public String logout(HttpServletRequest request){

        //清除session中的对象
        request.getSession().removeAttribute("user");

        //重定向和请求转发：视图解析器不会处理，因此使用全路径
        return "redirect:/signin.jsp";
    }

}
