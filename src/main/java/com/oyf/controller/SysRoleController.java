package com.oyf.controller;

import com.oyf.dto.SysAclModuleLevelDto;
import com.oyf.model.SysRole;
import com.oyf.model.SysUser;
import com.oyf.param.RoleParam;
import com.oyf.service.SysRoleService;
import com.oyf.tree.SysRole_AclTreeService;
import com.oyf.utils.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Create Time: 2019年03月25日 15:24
 * Create Author: 欧阳飞
 **/

@Controller
@RequestMapping("/sys/role")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysRole_AclTreeService sysRole_aclTreeService;


    @RequestMapping("/role.page")
    public ModelAndView entry(){

        return new ModelAndView("role");

    }

    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData save(RoleParam roleParam){

        sysRoleService.save(roleParam);

        return JsonData.success();
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData update(RoleParam roleParam){

        sysRoleService.update(roleParam);

        return JsonData.success();
    }


    @RequestMapping("/list.json")
    @ResponseBody
    public JsonData list(){

        List<SysRole> list = sysRoleService.list();

        return JsonData.success(list);
    }


    @RequestMapping("/roleTree.json")
    @ResponseBody
    public JsonData roleTree(Integer roleId){

        List<SysAclModuleLevelDto> roleAclTree = sysRole_aclTreeService.getRoleAclTree(roleId);

        return JsonData.success(roleAclTree);
    }

    @RequestMapping("/changeAcls.json")
    @ResponseBody
    public JsonData changeAcls(Integer roleId, String aclIds){

        sysRoleService.changeAcls(roleId,aclIds);

        return JsonData.success();
    }

    @RequestMapping("/users.json")
    @ResponseBody
    public JsonData users(Integer roleId){

        Map<String, List<SysUser>> users = sysRoleService.users(roleId);

        return JsonData.success(users);
    }

    @RequestMapping("/changeUsers.json")
    @ResponseBody
    public JsonData changeUsers(Integer roleId, String userIds){

        sysRoleService.changeUsers(roleId,userIds);

        return JsonData.success();
    }

}
