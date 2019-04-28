package com.oyf.service.impl;

import com.oyf.dao.SysAclMapper;
import com.oyf.dao.SysAclModuleMapper;
import com.oyf.exception.ParamException;
import com.oyf.model.SysAclModule;
import com.oyf.param.AclModuleParam;
import com.oyf.service.SysAclModuleService;
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
 * Create Time: 2019年03月23日 15:42
 * Create Author: 欧阳飞
 **/
@Service("sysAclModuleService")
public class SysAclModuleServiceImpl implements SysAclModuleService {

    @Resource
    private SysAclModuleMapper sysAclModuleMapper;

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysLogService sysLogService;

    @Override
    public void update(AclModuleParam aclModuleParam) {

        BeanValidator.check(aclModuleParam);
        //取出更新前的权限模块
        SysAclModule beforeModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleParam.getId());
        if (beforeModule == null){
            throw new ParamException("权限模块不存在！");
        }
        //更新前后的名字不一样,再进行判断
        if ( !beforeModule.getName().equalsIgnoreCase(aclModuleParam.getName())){
            if (checkIfExist(aclModuleParam.getParentId(), aclModuleParam.getName()) > 0){
                throw new ParamException("同一层级下不能有相同模块！");
            }
        }
        //封装要更新的数据
        SysAclModule afterModule = SysAclModule.builder().id(aclModuleParam.getId()).name(aclModuleParam.getName()).parentId(aclModuleParam.getParentId())
                .seq(aclModuleParam.getSeq()).remark(aclModuleParam.getRemark()).status(aclModuleParam.getStatus()).operateTime(new Date()).build();
        afterModule.setOperateIp(IpUtil.getUserIP(LoginHolder.getRequestHolder()));
        afterModule.setOperator(LoginHolder.getUserHolder().getUsername());
        afterModule.setLevel(LevelUtils.calculateLevel(getLevel(aclModuleParam.getParentId()),aclModuleParam.getParentId()));

        if (beforeModule.getId() == afterModule.getParentId()){
            throw new ParamException("不能修改为自己的子模块");
        }
        //递归更新
        updateWithChild(beforeModule,afterModule);
        sysLogService.saveAclModuleLog(beforeModule,afterModule);
    }

    @Override
    public void save(AclModuleParam aclModuleParam) {

        BeanValidator.check(aclModuleParam);
        if (checkIfExist(aclModuleParam.getParentId(),aclModuleParam.getName()) > 0){
            throw new ParamException("同一层级下不能有相同模块！");
        }
        //封装数据
        SysAclModule sysAclModule = SysAclModule.builder().parentId(aclModuleParam.getParentId()).remark(aclModuleParam.getRemark()).name(aclModuleParam.getName())
                .seq(aclModuleParam.getSeq()).status(aclModuleParam.getStatus()).operateTime(new Date()).build();
        sysAclModule.setOperator(LoginHolder.getUserHolder().getUsername());
        sysAclModule.setOperateIp(IpUtil.getUserIP(LoginHolder.getRequestHolder()));
        sysAclModule.setLevel(LevelUtils.calculateLevel(getLevel(aclModuleParam.getParentId()),aclModuleParam.getParentId()));
        //插入数据
        sysAclModuleMapper.insertSelective(sysAclModule);
        sysLogService.saveAclModuleLog(null,sysAclModule);

    }

    @Override
    public void delete(Integer id) {

        if (id == null){
            throw new ParamException("待删除的模块不存在");
        }
        //若模块下有子模块 则不能删除
        List<SysAclModule> childList = sysAclModuleMapper.findAllByLevel(LevelUtils.calculateLevel(getLevel(id), id));
        //若模块下有权限点 则不能删除
        int count = sysAclMapper.selectIfExistByModuleId(id);

        if ( childList.size() != 0 || count>0 ){
            throw new ParamException("删除失败！模块下有其它数据！");
        }
        //删除
        sysLogService.saveAclModuleLog(sysAclModuleMapper.selectByPrimaryKey(id),null);
        sysAclModuleMapper.deleteByPrimaryKey(id);

    }

    @Override
    public void updateWithChild(SysAclModule beforeModule,SysAclModule afterModule){
        String oldLevel = beforeModule.getLevel();
        String newLevel = afterModule.getLevel();
        //如果层级不相等 要更新子模块
        if ( !oldLevel.equals(newLevel)){
            //获取子模块集合
            List<SysAclModule> childAclModuleList =  sysAclModuleMapper.findAllByLevel(LevelUtils.calculateLevel(oldLevel,beforeModule.getId()));
            //遍历集合 为每个模块更新层级
            for (SysAclModule afterChild : childAclModuleList){
                //保存子模块更新前的数据
                SysAclModule beforeChild = new SysAclModule();
                BeanUtils.copyProperties(afterChild,beforeChild);
                //子模块的新层级
                afterChild.setLevel(LevelUtils.calculateLevel(newLevel,afterModule.getId()));
                //递归
                updateWithChild(beforeChild,afterChild);
            }
        }
        sysAclModuleMapper.updateByPrimaryKey(afterModule);
    }


    /*根据权限id获取权限的level*/
    public String getLevel(Integer id){

        SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(id);
        if (sysAclModule == null)return null;
        return sysAclModule.getLevel();
    }

    /*根据父模块id和自己的名字 查询同一权限模块下是否有相同模块*/
    public int checkIfExist(Integer parentId,String name){

        return sysAclModuleMapper.selectIfExist(parentId,name);
    }

}
