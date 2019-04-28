package com.oyf.tree;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.oyf.dao.SysAclModuleMapper;
import com.oyf.dto.SysAclModuleLevelDto;
import com.oyf.model.SysAclModule;
import com.oyf.utils.LevelUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Create Time: 2019年03月23日 15:52
 * Create Author: 欧阳飞
 **/

/*
*
* 用来生成权限模块树
*
* */


@Service
public class SysAclModuleTreeService {

    @Resource
    private SysAclModuleMapper sysAclModuleMapper;


    public List<SysAclModuleLevelDto> getAclModuleTree(){

        //查询出所有的权限模块数据
        List<SysAclModule> sysAclModuleList = sysAclModuleMapper.findAll();
        //将其中的数据封装进dto，再存入dto集合
        List<SysAclModuleLevelDto> dtoList = new ArrayList<>();
        for (SysAclModule sysAclModule : sysAclModuleList){
            dtoList.add(SysAclModuleLevelDto.adapter(sysAclModule));
        }

        return toTree(dtoList);
    }

    public List<SysAclModuleLevelDto> toTree(List<SysAclModuleLevelDto> dtoList){
        //如果没有权限模块
        if (dtoList == null){
            return new ArrayList<SysAclModuleLevelDto>();
        }
        //顶层权限模块集合
        List<SysAclModuleLevelDto> rootList = new ArrayList<>();
        //所有权限模块集合
        Multimap<String, SysAclModuleLevelDto> multimap = ArrayListMultimap.create();

        for (SysAclModuleLevelDto dto : dtoList){
            if (dto.getLevel().equals(LevelUtils.ROOT)){//证明是顶层权限模块
                rootList.add(dto);
            }
            multimap.put(dto.getLevel(),dto);//按层级存储所有权限模块
        }

        //排序
        Collections.sort(rootList,new MyComparator());
        recursionAclModuleTree(rootList,multimap);

        return rootList;
    }

    //递归调用生成权限模块树
    public void recursionAclModuleTree(List<SysAclModuleLevelDto> rootList, Multimap<String, SysAclModuleLevelDto> multimap){

        //获取rootList中每个元素，判断是否还有下级，有下级就递归
        for (SysAclModuleLevelDto dto : rootList){
            //通过层级,从map中取出判断
            String nextLevel = LevelUtils.calculateLevel(dto.getLevel(),dto.getId());
            List<SysAclModuleLevelDto> childList = (List<SysAclModuleLevelDto>) multimap.get(nextLevel);
            //如果还有下一层
            if (childList != null){
                //排序
                Collections.sort(childList,new MyComparator());
                //设置把下级集合设置进上级模块
                dto.setAclModuleList(childList);
                //递归
                recursionAclModuleTree(childList,multimap);
            }
        }
    }

    public class MyComparator implements Comparator<SysAclModuleLevelDto>{

        @Override
        public int compare(SysAclModuleLevelDto o1, SysAclModuleLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    }



}
