package com.oyf.service.impl;

import com.oyf.dao.SysDeptMapper;
import com.oyf.dao.SysUserMapper;
import com.oyf.exception.ParamException;
import com.oyf.model.SysDept;
import com.oyf.param.DeptParam;
import com.oyf.service.SysDeptService;
import com.oyf.service.SysLogService;
import com.oyf.utils.BeanValidator;
import com.oyf.utils.IpUtil;
import com.oyf.utils.LevelUtils;
import com.oyf.utils.LoginHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Create Time: 2019年03月21日 16:11
 * Create Author: 欧阳飞
 **/

@Service("sysDeptService")
public class SysDeptServiceImpl implements SysDeptService {


    @Resource
    private SysDeptMapper sysDeptMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysLogService sysLogService;


    @Override
    public void save(DeptParam deptParam) {
        //校验参数
        BeanValidator.check(deptParam);
        if (checkExist(deptParam.getParentId(),deptParam.getName()) > 0){
            throw new ParamException("同一层级下不能有相同部门！");
        }
        SysDept sysDept = SysDept.builder().name(deptParam.getName()).parentId(deptParam.getParentId())
                .remark(deptParam.getRemark()).seq(deptParam.getSeq()).operateTime(new Date()).build();
        //从ThreadLocal中取操作者name和request
        sysDept.setOperator(LoginHolder.getUserHolder().getUsername());
        sysDept.setOperateIp(IpUtil.getUserIP(LoginHolder.getRequestHolder()));
        //通过计算获得 当前部门的层级 = 父部门Level.父部门Id
        sysDept.setLevel(LevelUtils.calculateLevel(getLevel(deptParam.getParentId()),deptParam.getParentId()));
        sysDeptMapper.insertSelective(sysDept);
        sysLogService.saveDeptLog(null,sysDept);
    }

    @Override
    public void update(DeptParam deptParam) {
        //校验参数
        BeanValidator.check(deptParam);
        //根据id取出更新前的部门
        SysDept beforeDept = sysDeptMapper.selectByPrimaryKey(deptParam.getId());
        if (beforeDept == null){
            throw new ParamException("部门不存在！");
        }
        //如果更新前后名称不一样，才进行判断
        if ( !beforeDept.getName().equalsIgnoreCase(deptParam.getName())){
            if (checkExist(deptParam.getParentId(),deptParam.getName()) > 0){
                throw new ParamException("同一层级下不能有相同部门！");
            }
        }

        //设置要更新成的新部门
        SysDept afterDept = SysDept.builder().id(deptParam.getId()).name(deptParam.getName()).parentId(deptParam.getParentId())
                .remark(deptParam.getRemark()).seq(deptParam.getSeq()).operateTime(new Date()).build();
        afterDept.setOperator(LoginHolder.getUserHolder().getUsername());
        afterDept.setOperateIp(IpUtil.getUserIP(LoginHolder.getRequestHolder()));
        afterDept.setLevel(LevelUtils.calculateLevel(getLevel(deptParam.getParentId()),deptParam.getParentId()));

        if (beforeDept.getId() == afterDept.getParentId()){
            throw new ParamException("不能修改为自己的子部门！");
        }

        updateWithChild(beforeDept,afterDept);
        sysLogService.saveDeptLog(beforeDept,afterDept);
    }

    @Override
    public void delete(Integer id) {
        if (id == null){
            throw new ParamException("待删除的部门不存在！");
        }
        //获取要删除的部门的level
        String level = getLevel(id);
        //通过level.id 拼接成下级部门的层级
        String childLevel = LevelUtils.calculateLevel(level, id);
        System.out.println(childLevel);
        //通过childlevel查找下级部门
        List<SysDept> childDepts = sysDeptMapper.selectByLevel(childLevel);
        //通过部门id查找部门员工
        int count = sysUserMapper.checkIfExistByDeptId(id);
        if ( childDepts.size() != 0 || count > 0){
            throw new ParamException("删除失败！该部门下有其它信息！");
        }
        //删除
        sysLogService.saveDeptLog(sysDeptMapper.selectByPrimaryKey(id),null);
        sysDeptMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateWithChild(SysDept beforeDept, SysDept afterDept){
        String oldLevel = beforeDept.getLevel();
        String newLevel = afterDept.getLevel();
        //如果层级不相等，需要更新子部门
        if ( !oldLevel.equals(newLevel)){
            //更新子部门，先获取所有子部门，通过当前层级的level+id 得到子部门层级的集合
            List<SysDept> childDept = sysDeptMapper.selectByLevel(oldLevel + LevelUtils.SEPARATOR + beforeDept.getId());
            //如果有子部门，则修改所有子部门的层级
            if (childDept != null){
                //给所有子部门设置新的level(通过递归一个层级一个层级的设置)
                for (SysDept sysDept : childDept){
                    //用来保存子部门更新前的数据
                    SysDept beforeChild = new SysDept();
                    BeanUtils.copyProperties(sysDept,beforeChild);
                    //更新子部门
                    sysDept.setLevel(newLevel + LevelUtils.SEPARATOR + afterDept.getId());//eg:0.1---->0.12
                    //递归
                    updateWithChild(beforeChild,sysDept);
                }
            }
        }
        sysDeptMapper.updateByPrimaryKey(afterDept);
    }

    /*验证同一部门层级下是否有相同的部门*/
    public int checkExist(Integer parentId,String name){
         return sysDeptMapper.selectDeptIfExist(parentId, name);
    }

    /*根据部门Id获取部门的Level*/
    public String getLevel(Integer parentId){
        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(parentId);
        if (sysDept == null){
            return null;
        }
        return sysDept.getLevel();

    }


}
