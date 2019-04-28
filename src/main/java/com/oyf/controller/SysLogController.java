package com.oyf.controller;

import com.oyf.beans.PageBean;
import com.oyf.model.SysLogWithBLOBs;
import com.oyf.param.LogParam;
import com.oyf.service.SysLogService;
import com.oyf.utils.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Create Time: 2019年04月01日 14:30
 * Create Author: 欧阳飞
 **/

@Controller
@RequestMapping("/sys/log")
public class SysLogController {

    @Resource
    SysLogService sysLogService;

    @RequestMapping("/log.page")
    public ModelAndView entry(){
        return new ModelAndView("log");
    }

    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData page(LogParam logParam, PageBean<SysLogWithBLOBs> pageBean){

        PageBean<SysLogWithBLOBs> pb = sysLogService.getPageBean(logParam, pageBean);

        return JsonData.success(pb);
    }

    @RequestMapping("/recover.json")
    @ResponseBody
    public JsonData recover(Integer id){

        sysLogService.recover(id);

        return JsonData.success();
    }


}
