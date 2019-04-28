package com.oyf.tree;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oyf.beans.CacheKeyPrefix;
import com.oyf.dao.SysAclMapper;
import com.oyf.dao.SysRoleAclMapper;
import com.oyf.dao.SysRoleUserMapper;
import com.oyf.model.SysAcl;
import com.oyf.service.SysCacheService;
import com.oyf.utils.JsonMapper;
import com.oyf.utils.LoginHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Create Time: 2019年03月27日 14:29
 * Create Author: 欧阳飞
 **/

/*
*       核心功能
*
* */

@Service
public class SysCoreService {

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysRoleAclMapper sysRoleAclMapper;

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    @Resource
    private SysCacheService sysCacheService;


    //通过当前登录用户的id，获取该用户的角色Id，再通过角色查询出角色对应的权限点
    public List<SysAcl> getUserRoleAclList(){

        //获取当前用户的userId
        Integer userId = LoginHolder.getUserHolder().getId();
        //判断是否超级管理员（所有权限）
        if (isSuperAdmin()){
            List<SysAcl> aclList = sysAclMapper.findAll();
            return aclList;
        }
        //通过userId在sys_role_user表中查询该用户对应的roleId
        List<Integer> roleIds = sysRoleUserMapper.selectRoleIdsByUserId(userId);
        if (roleIds == null || roleIds.size() == 0){//当前用户没有对应角色
            return new ArrayList<>();
        }
        //通过roleIds在sys_role_acl表中查询对应的权限点id
        List<Integer> aclIds = sysRoleAclMapper.findAclIdsByRoleIds(roleIds);
        if (aclIds == null || roleIds.size() == 0){//当前角色没有对应的权限点id
            return new ArrayList<>();
        }
        //通过aclIds在sys_acl表中查询对应的权限点
        List<SysAcl> aclList = sysAclMapper.findAllByAclIds(aclIds);

        return aclList;
    }

    //通过roleId获取当前角色拥有的权限
    public List<SysAcl> getRoleList(Integer roleId){

        ArrayList<Integer> roleIds = new ArrayList<>();
        roleIds.add(roleId);
        List<Integer> aclIds = sysRoleAclMapper.findAclIdsByRoleIds(roleIds);
        if (aclIds == null || aclIds.size()==0){
            return new ArrayList<>();
        }
        return sysAclMapper.findAllByAclIds(aclIds);

    }


    public boolean isSuperAdmin(){
        if (LoginHolder.getUserHolder().getId() == 1)return true;
        return false;
    }

    /*判断当前用户有哪些权限（url）*/
    public boolean hasAcl(String url){
        //如果是超级管理员
        if (isSuperAdmin())return true;

        //调用本类方法获取当前用户的权限点集合
        //List<SysAcl> userRoleAclList = getUserRoleAclList();

        //通过缓存读取权限点集合
        List<SysAcl> userRoleAclList = getCurrentUserAclFromCache();

        //通过url查询权限点
        SysAcl sysAcl = sysAclMapper.findAclByUrl(url);

        if (sysAcl == null){//说明不需要权限也能访问
            return true;
        }
        if (userRoleAclList.contains(sysAcl)){//如果用户有这个权限点 放行
            return true;
        }
        return false;
    }


    //从缓存中取出当前用户的权限点集合
    public List<SysAcl> getCurrentUserAclFromCache(){

        //先从缓存中取用户的权限点json
        String aclJson = sysCacheService.getFromCache(LoginHolder.getUserHolder().getUsername(), CacheKeyPrefix.USER_ACLS);
        if (aclJson == null){//如果缓存没有，就从数据库中取,并存入缓存
            System.err.println("缓存没有，数据库中取,并存入缓存");
            List<SysAcl> userRoleAclList = getUserRoleAclList();
            String json = JsonMapper.obj2String(userRoleAclList);
            sysCacheService.saveIntoCache(json,60*60*24,LoginHolder.getUserHolder().getUsername(),CacheKeyPrefix.USER_ACLS);
            return userRoleAclList;
        }
        //缓存中有
        System.err.println("缓存中有");
        List<SysAcl> sysAcl = JsonMapper.string2Obj(aclJson, new TypeReference<List<SysAcl>>() {});
        return sysAcl;
    }



}
