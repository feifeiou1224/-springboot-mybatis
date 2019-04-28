package com.oyf.tree;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.oyf.dao.SysDeptMapper;
import com.oyf.dto.SysDeptLevelDto;
import com.oyf.model.SysDept;
import com.oyf.utils.LevelUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Create Time: 2019年03月21日 19:05
 * Create Author: 欧阳飞
 **/

/*
*   用来生成部门树
*
* */

@Service
public class SysDeptTreeService {

    @Resource
    private SysDeptMapper sysDeptMapper;

    public List<SysDeptLevelDto> getDeptTree(){
        //先查询出所有的部门
        List<SysDept> deptList = sysDeptMapper.findAll();
        //将集合中的所有dept转成deptdto 再存入另一个集合
        List<SysDeptLevelDto> dtoList = new ArrayList<>();
        for (SysDept sysDept : deptList){
            dtoList.add(SysDeptLevelDto.adapter(sysDept));
        }

        return getClassifyDeptTree(dtoList);

    }

    /*将顶层部门 和 下级部门 分类封装进两个集合*/
    public List<SysDeptLevelDto> getClassifyDeptTree(List<SysDeptLevelDto> dtoList){
        if (dtoList == null){//如果没有部门
            return new ArrayList<SysDeptLevelDto>();
        }
        //按部门封装所有部门
        Multimap<String, SysDeptLevelDto> multimap = ArrayListMultimap.create();//key值相同，会将value封装成集合
        //只封装顶层部门
        List<SysDeptLevelDto> rootList = new ArrayList<>();
        //通过查询出的所有部门开始封装数据
        for (SysDeptLevelDto sysDeptLevelDto : dtoList){
            if (sysDeptLevelDto.getLevel().equals(LevelUtils.ROOT)){//如果是顶层部门
                rootList.add(sysDeptLevelDto);
            }
            //封装所有部门
            multimap.put(sysDeptLevelDto.getLevel(),sysDeptLevelDto);
        }

        Collections.sort(rootList,new MyComparator());//根据seq排序
        recursionDeptTree(rootList,multimap);

        return rootList;
    }

    /*通过递归调用生成部门树*/
    public void recursionDeptTree(List<SysDeptLevelDto> rootList, Multimap<String, SysDeptLevelDto> multimap){

        //获取rootList中的每一个元素
        for (int i = 0;i<rootList.size();i++){
            SysDeptLevelDto sysDeptLevelDto = rootList.get(i);
            //当前部门的 level.id 就是下级部门的level
            String nexLevel = LevelUtils.calculateLevel(sysDeptLevelDto.getLevel(),sysDeptLevelDto.getId());
            //通过下一层级的level，从map中获取下一级的部门集合
            List<SysDeptLevelDto> nextDeptLevelList = (List<SysDeptLevelDto>) multimap.get(nexLevel);

            if (nextDeptLevelList != null){//如果下一层级还有部门
                //排序
                Collections.sort(nextDeptLevelList,new MyComparator());
                //设置下一层级部门的集合
                sysDeptLevelDto.setDeptList(nextDeptLevelList);
                //递归
                recursionDeptTree(nextDeptLevelList,multimap);
            }
        }
    }

    public class MyComparator implements Comparator<SysDeptLevelDto>{

        @Override
        public int compare(SysDeptLevelDto o1, SysDeptLevelDto o2) {
            return o1.getSeq()-o2.getSeq();
        }
    }

}
