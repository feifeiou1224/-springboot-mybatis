package com.oyf.tree;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.oyf.dao.SysAclMapper;
import com.oyf.dto.SysAclDto;
import com.oyf.dto.SysAclModuleLevelDto;
import com.oyf.model.SysAcl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Create Time: 2019年03月27日 10:58
 * Create Author: 欧阳飞
 **/

/*
*   生成角色权限树
*
* */

@Service
public class SysRole_AclTreeService {

    @Resource
    private SysCoreService sysCoreService;

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysAclModuleTreeService sysAclModuleTreeService;


    public List<SysAclModuleLevelDto> getRoleAclTree(Integer roleId){
        //获取当前用户拥有的权限点
        List<SysAcl> userRoleAclList = sysCoreService.getUserRoleAclList();
        //获取点击的角色对应的权限点
        List<SysAcl> roleList = sysCoreService.getRoleList(roleId);
        //获取所有权限点
        List<SysAcl> all = sysAclMapper.findAll();
        //存储aclDto
        List<SysAclDto> aclDtos = new ArrayList<>();
        //遍历所有权限点
        for (SysAcl sysAcl : all){
            SysAclDto dto = SysAclDto.adpater(sysAcl);
            //如果当前用户有操作权限
            if (userRoleAclList.contains(sysAcl)){
                dto.setHasAcl(true);
            }
            //如果角色也有操作权限
            if (roleList.contains(sysAcl)){
                dto.setChecked(true);
            }
            aclDtos.add(dto);
        }

        return aclDtoToTree(aclDtos);
    }

    public List<SysAclModuleLevelDto> aclDtoToTree(List<SysAclDto> aclDtos){
        if (aclDtos == null || aclDtos.size() == 0){
            return new ArrayList<>();
        }
        //将权限点存储到对应的权限模块下
        //获取权限模块树
        List<SysAclModuleLevelDto> aclModuleTree = sysAclModuleTreeService.getAclModuleTree();
        //key为权限点模块的id，value为权限点dto对象
        Multimap<Integer, SysAclDto> multimap = ArrayListMultimap.create();

        for (SysAclDto sysAclDto : aclDtos){
            //判断权限点的状态
            if (sysAclDto.getStatus() == 1){
                multimap.put(sysAclDto.getAclModuleId(),sysAclDto);
            }
        }
        //递归
        bindAclToAclModule(aclModuleTree,multimap);
        return aclModuleTree;
    }

    /*递归绑定权限点到权限模块下*/
    public void bindAclToAclModule(List<SysAclModuleLevelDto> aclModuleTree, Multimap<Integer, SysAclDto> multimap){
        if (aclModuleTree == null || aclModuleTree.size() == 0){
            return;
        }
        //遍历权限模块树 封装数据
        for (SysAclModuleLevelDto dto : aclModuleTree){
            //通过权限模块id取出权限点集合
            List<SysAclDto> sysAclDtos = (List<SysAclDto>) multimap.get(dto.getId());
            //排序
            Collections.sort(sysAclDtos,new MyComparator());
            dto.setAclList(sysAclDtos);
            bindAclToAclModule(dto.getAclModuleList(),multimap);
        }
    }

    public class MyComparator implements Comparator<SysAclDto>{

        @Override
        public int compare(SysAclDto o1, SysAclDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    }

}
