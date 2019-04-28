package com.oyf.service;

import com.oyf.model.SysRole;
import com.oyf.model.SysUser;
import com.oyf.param.RoleParam;

import java.util.List;
import java.util.Map;

public interface SysRoleService {


    void save(RoleParam roleParam);

    void update(RoleParam roleParam);

    List<SysRole> list();

    void changeAcls(Integer roleId, String aclIds);

    Map<String, List<SysUser>> users(Integer roleId);

    void changeUsers(Integer roleId, String userIds);

}
