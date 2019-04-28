package com.oyf.controller;

import com.oyf.dto.SysAclModuleLevelDto;
import com.oyf.param.AclModuleParam;
import com.oyf.service.SysAclModuleService;
import com.oyf.tree.SysAclModuleTreeService;
import com.oyf.utils.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Create Time: 2019年03月23日 15:25
 * Create Author: 欧阳飞
 **/

@Controller
@RequestMapping("/sys/aclModule")
public class SysAclModuleController {
    
    @Resource
    private SysAclModuleService sysAclModuleService;
    
    @Resource
    private SysAclModuleTreeService sysAclModuleTreeService;


    @RequestMapping("/acl.page")
    public ModelAndView entry(){

        return new ModelAndView("acl");

    }

    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData aclModuleTree(){

        List<SysAclModuleLevelDto> aclModuleList = sysAclModuleTreeService.getAclModuleTree();

        return JsonData.success(aclModuleList);
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData update(AclModuleParam aclModuleParam){

        sysAclModuleService.update(aclModuleParam);

        return JsonData.success();
    }

    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData save(AclModuleParam aclModuleParam){

        sysAclModuleService.save(aclModuleParam);

        return JsonData.success();
    }

    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData delete(Integer id){

        sysAclModuleService.delete(id);

        return JsonData.success();
    }

}
