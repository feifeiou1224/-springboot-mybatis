package com.oyf.service.impl;

import com.oyf.dao.SysRoleAclMapper;
import com.oyf.dao.SysRoleMapper;
import com.oyf.dao.SysRoleUserMapper;
import com.oyf.dao.SysUserMapper;
import com.oyf.exception.ParamException;
import com.oyf.model.*;
import com.oyf.param.RoleParam;
import com.oyf.service.SysLogService;
import com.oyf.service.SysRoleService;
import com.oyf.tree.SysCoreService;
import com.oyf.utils.BeanValidator;
import com.oyf.utils.IpUtil;
import com.oyf.utils.LoginHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Create Time: 2019年03月25日 15:56
 * Create Author: 欧阳飞
 **/

@Service("sysRoleService")
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysCoreService sysCoreService;

    @Resource
    private SysRoleAclMapper sysRoleAclMapper;

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysLogService sysLogService;

    @Override
    public void save(RoleParam roleParam) {

        BeanValidator.check(roleParam);
        //判断是否有重复的角色名
        int count = sysRoleMapper.selectIfExistByName(roleParam.getName());
        if ( count > 0 ){
            throw new ParamException("添加失败！角色名重复！");
        }
        //封装数据
        SysRole sysRole = SysRole.builder().name(roleParam.getName()).remark(roleParam.getRemark()).status(roleParam.getStatus())
        .type(roleParam.getType()).operateTime(new Date()).build();
        sysRole.setOperator(LoginHolder.getUserHolder().getUsername());
        sysRole.setOperateIp(IpUtil.getUserIP(LoginHolder.getRequestHolder()));
        //添加
        sysRoleMapper.insertSelective(sysRole);
        sysLogService.saveRoleLog(null,sysRole);
    }

    @Override
    public void update(RoleParam roleParam) {

        BeanValidator.check(roleParam);
        //通过id查询原角色数据
        SysRole beforeRole = sysRoleMapper.selectByPrimaryKey(roleParam.getId());
        //修改前后的角色名称不一样，则判断角色名是否重复
        if ( !beforeRole.getName().equals(roleParam.getName()) ){
            if ( sysRoleMapper.selectIfExistByName(roleParam.getName()) > 0){
                throw new ParamException("修改失败！角色名重复！");
            }
        }
        //封装数据
        SysRole afterRole = SysRole.builder().name(roleParam.getName()).remark(roleParam.getRemark()).status(roleParam.getStatus())
        .type(roleParam.getType()).operateTime(new Date()).id(roleParam.getId()).build();
        afterRole.setOperator(LoginHolder.getUserHolder().getUsername());
        afterRole.setOperateIp(IpUtil.getUserIP(LoginHolder.getRequestHolder()));
        //更新
        sysRoleMapper.updateByPrimaryKeySelective(afterRole);
        sysLogService.saveRoleLog(beforeRole,afterRole);

    }

    @Override
    public List<SysRole> list() {

        //查询所有角色
        List<SysRole> roles = sysRoleMapper.findAll();

        return roles;
    }

    @Override
    @Transactional
    public void changeAcls(Integer roleId, String aclIds) {

        if (aclIds == null ) {
            throw new ParamException("修改失败！未知错误！");
        }
        if (roleId == null){
            throw new ParamException("待修改角色不存在！");
        }
        if ( aclIds.equals("") ){//如果没有打勾，说明删除所有
            sysRoleAclMapper.deleteByRoleId(roleId);
            return;
        }

        //将获取到的字符串转为集合
        List<String> newRoleList = Arrays.asList(aclIds.split(","));
        //通过roleId获取该角色的权限点集合
        List<SysAcl> roleList = sysCoreService.getRoleList(roleId);
        //将权限点id封装到集合
        List<String> oldRoleList = new ArrayList<>();
        for (int i = 0; i < roleList.size(); i++) {
            oldRoleList.add(roleList.get(i).getId()+"");
        }

        //将之前的String集合封装为Integer集合
        List<Integer> before = new ArrayList<>();
        for (int i = 0; i < roleList.size(); i++) {
            before.add(roleList.get(i).getId());
        }
        List<Integer> after = new ArrayList<>();
        for (int i = 0; i < newRoleList.size(); i++) {
            after.add(Integer.parseInt(newRoleList.get(i)));
        }


        if (newRoleList.size() == oldRoleList.size()) {//如果长度相等,需要判断内容是否改变
            //将原来权限点 根据 新的权限点移除，如果移除后为空，则没有发生修改
            oldRoleList.removeAll(newRoleList);
            if (oldRoleList.size() == 0) {//内容没有发生修改
                throw new ParamException("权限点没有发生修改！");
            }
        }
        //其它情况 都要修改内容：先删除角色的权限点信息，再根据新的添加
        sysRoleAclMapper.deleteByRoleId(roleId);
        //用来存储要插入的数据集合
        ArrayList<SysRoleAcl> sysRoleAcls = new ArrayList<>();
        for (int i = 0; i < newRoleList.size(); i++) {
            SysRoleAcl sysRoleAcl = SysRoleAcl.builder().roleId(roleId).aclId(Integer.parseInt(newRoleList.get(i)))
            .operateIp(IpUtil.getUserIP(LoginHolder.getRequestHolder())).operateTime(new Date()).operator(LoginHolder.getUserHolder().getUsername()).build();
            sysRoleAcls.add(sysRoleAcl);
            //sysRoleAclMapper.insertSelective(sysRoleAcl);
        }
        //批量添加(相比一条一条添加更有效率，占用资源更少)
        sysRoleAclMapper.batchInsert(sysRoleAcls);
        sysLogService.saveRoleAclLog(roleId,before,after);
    }

    /*当前角色拥有的用户 和未拥有的用户 封装为Map集合*/
    @Override
    public Map<String, List<SysUser>> users(Integer roleId) {

        //获取所有用户
        List<SysUser> all = sysUserMapper.findAll();

        //通过roleId获取userIds
        List<Integer> userIds = sysRoleUserMapper.getUserIdsByRoleId(roleId);
        if (userIds.size() == 0 || userIds == null){
            Map<String, List<SysUser>> map = new HashMap<>();
            map.put("selected",null);
            map.put("unselected",all);
            return map;
        }
        //通过userIds获取user集合（角色拥有的用户）
        List<SysUser> users = sysUserMapper.findByUserIds(userIds);
        //角色未拥有的用户 = 全部-已拥有的
        all.removeAll(users);
        //将已拥有的和未拥有的封装进集合
        Map<String, List<SysUser>> map = new HashMap<>();
        map.put("selected",users);
        map.put("unselected",all);

        return map;
    }

    @Transactional
    @Override
    public void changeUsers(Integer roleId, String userIds) {

        if (userIds == null ) {
            throw new ParamException("修改失败！未知错误！");
        }
        if (roleId == null ){
            throw new ParamException("待修改角色不存在！");
        }
        if (userIds.equals("")){//如果没有字符串，说明都没有勾选-->删除所有
            sysRoleUserMapper.deleteByRoleId(roleId);
            return;
        }
        //将获取到的字符串转换为集合
        List<Integer> newUserIds = new ArrayList<>();
        String[] splits = userIds.split(",");
        for (int i = 0; i < splits.length; i++) {
            newUserIds.add(Integer.parseInt(splits[i]));
        }
        //通过roleId查询出该角色拥有的用户的id
        List<Integer> oldUserIds = sysRoleUserMapper.getUserIdsByRoleId(roleId);
        //判断集合长度
        if (newUserIds.size() == oldUserIds.size()){
            oldUserIds.removeAll(newUserIds);
            if (oldUserIds.size() == 0){//说明修改前后一样
                throw new ParamException("没有修改角色用户！");
            }
        }
        //根据roleId从sys_role_user表中删除所有对应数据
        sysRoleUserMapper.deleteByRoleId(roleId);
        //其它情况还要再把新的添加进去，完成更新
        List<SysRoleUser> sysRoleUsers = new ArrayList<>();
        for (int i = 0; i < newUserIds.size(); i++) {
            SysRoleUser sysRoleUser = SysRoleUser.builder().roleId(roleId).userId(newUserIds.get(i)).operateTime(new Date())
                    .operateIp(IpUtil.getUserIP(LoginHolder.getRequestHolder())).operator(LoginHolder.getUserHolder().getUsername()).build();
            sysRoleUsers.add(sysRoleUser);
        }
        //批量更新
        sysRoleUserMapper.batchInsert(sysRoleUsers);
        sysLogService.saveRoleUserLog(roleId,oldUserIds,newUserIds);

    }


}
