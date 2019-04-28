package com.oyf.controller;

import com.oyf.beans.PageBean;
import com.oyf.model.SysAcl;
import com.oyf.param.AclParam;
import com.oyf.service.SysAclService;
import com.oyf.utils.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Create Time: 2019年03月25日 10:42
 * Create Author: 欧阳飞
 **/


@Controller
@RequestMapping("/sys/acl")
public class SysAclController {

    @Resource
    private SysAclService sysAclService;

    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData page(Integer aclModuleId, PageBean<SysAcl> pageBean){

        PageBean<SysAcl> pageBeanById = sysAclService.getPageBeanById(aclModuleId, pageBean);

        return JsonData.success(pageBeanById);
    }

    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData save(AclParam aclParam){

        sysAclService.save(aclParam);

        return JsonData.success();
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData update(AclParam aclParam){

        sysAclService.update(aclParam);

        return JsonData.success();
    }

    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData delete(Integer id){

        sysAclService.delete(id);

        return JsonData.success();
    }


}
