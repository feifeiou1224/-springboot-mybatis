package com.oyf.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oyf.beans.LogType;
import com.oyf.beans.PageBean;
import com.oyf.dao.*;
import com.oyf.dto.SysLogDto;
import com.oyf.exception.ParamException;
import com.oyf.model.*;
import com.oyf.param.*;
import com.oyf.service.*;
import com.oyf.utils.BeanValidator;
import com.oyf.utils.IpUtil;
import com.oyf.utils.JsonMapper;
import com.oyf.utils.LoginHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Create Time: 2019年04月01日 14:37
 * Create Author: 欧阳飞
 **/

@Service("sysLogService")
public class SysLogServiceImpl implements SysLogService {

    @Resource
    private SysLogMapper sysLogMapper;

    @Resource
    private SysDeptService sysDeptService;
    @Resource
    private SysDeptMapper sysDeptMapper;

    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysAclModuleMapper sysAclModuleMapper;
    @Resource
    private SysAclModuleService sysAclModuleService;

    @Resource
    private SysAclMapper sysAclMapper;
    @Resource
    private SysAclService sysAclService;


    @Override
    public PageBean<SysLogWithBLOBs> getPageBean(LogParam logParam, PageBean<SysLogWithBLOBs> pageBean) {

        BeanValidator.check(pageBean);

        SysLogDto sysLogDto = new SysLogDto();
        sysLogDto.setType(logParam.getType());

        if (StringUtils.isNotBlank(logParam.getBeforeSeg())){
            sysLogDto.setBeforeSeg(logParam.getBeforeSeg());
        }
        if (StringUtils.isNotBlank(logParam.getAfterSeg())){
            sysLogDto.setAfterSeg(logParam.getAfterSeg());
        }
        if (StringUtils.isNotBlank(logParam.getOperator())){
            sysLogDto.setOperator(logParam.getOperator());
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (StringUtils.isNotBlank(logParam.getFromTime())){
                sysLogDto.setFromTime(format.parse(logParam.getFromTime()));
            }
            if (StringUtils.isNotBlank(logParam.getToTime())){
                sysLogDto.setToTime(format.parse(logParam.getToTime()));
            }
        }catch (Exception e){
            throw new ParamException("日期格式有误");
        }
        //查询log的条数
        int count = sysLogMapper.countByLogDto(sysLogDto);
        if (count > 0){
            //根据条件查询出相应的log集合
            List<SysLogWithBLOBs> data = sysLogMapper.getLogListByLogDto(sysLogDto,pageBean);
            PageBean<SysLogWithBLOBs> syspb = new PageBean<>();
            syspb.setData(data);
            syspb.setTotal(count);
            return syspb;
        }

        return new PageBean<>();
    }

    @Override
    public void saveDeptLog(SysDept before, SysDept after) {

        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();

        sysLogWithBLOBs.setType( LogType.TYPE_DEPT.getType() );
        sysLogWithBLOBs.setTargetId( after==null?before.getId():after.getId() );//增加时before没有id，删除时after没有id
        sysLogWithBLOBs.setOldValue( before==null?"": JsonMapper.obj2String(before));//增加时没有老数据
        sysLogWithBLOBs.setNewValue( after==null?"": JsonMapper.obj2String(after));//删除时没有新数据
        sysLogWithBLOBs.setOperator( LoginHolder.getUserHolder().getUsername());
        sysLogWithBLOBs.setOperateIp(IpUtil.getUserIP(LoginHolder.getRequestHolder()));
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(0);

        //保存日志
        sysLogMapper.insert(sysLogWithBLOBs);

    }

    @Override
    public void saveUserLog(SysUser before, SysUser after) {

        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();

        sysLogWithBLOBs.setType( LogType.TYPE_USER.getType() );
        sysLogWithBLOBs.setTargetId( after==null?before.getId():after.getId() );//增加时before没有id，删除时after没有id
        sysLogWithBLOBs.setOldValue( before==null?"": JsonMapper.obj2String(before));//增加时没有老数据
        sysLogWithBLOBs.setNewValue( after==null?"": JsonMapper.obj2String(after));//删除时没有新数据
        sysLogWithBLOBs.setOperator( LoginHolder.getUserHolder().getUsername());
        sysLogWithBLOBs.setOperateIp(IpUtil.getUserIP(LoginHolder.getRequestHolder()));
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(0);

        //保存日志
        sysLogMapper.insert(sysLogWithBLOBs);
    }

    @Override
    public void saveAclModuleLog(SysAclModule before, SysAclModule after) {

        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();

        sysLogWithBLOBs.setType( LogType.TYPE_ACL_MODULE.getType() );
        sysLogWithBLOBs.setTargetId( after==null?before.getId():after.getId() );//增加时before没有id，删除时after没有id
        sysLogWithBLOBs.setOldValue( before==null?"": JsonMapper.obj2String(before));//增加时没有老数据
        sysLogWithBLOBs.setNewValue( after==null?"": JsonMapper.obj2String(after));//删除时没有新数据
        sysLogWithBLOBs.setOperator( LoginHolder.getUserHolder().getUsername());
        sysLogWithBLOBs.setOperateIp(IpUtil.getUserIP(LoginHolder.getRequestHolder()));
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(0);

        //保存日志
        sysLogMapper.insert(sysLogWithBLOBs);

    }

    @Override
    public void saveAclLog(SysAcl before, SysAcl after) {

        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();

        sysLogWithBLOBs.setType( LogType.TYPE_ACL.getType() );
        sysLogWithBLOBs.setTargetId( after==null?before.getId():after.getId() );//增加时before没有id，删除时after没有id
        sysLogWithBLOBs.setOldValue( before==null?"": JsonMapper.obj2String(before));//增加时没有老数据
        sysLogWithBLOBs.setNewValue( after==null?"": JsonMapper.obj2String(after));//删除时没有新数据
        sysLogWithBLOBs.setOperator( LoginHolder.getUserHolder().getUsername());
        sysLogWithBLOBs.setOperateIp(IpUtil.getUserIP(LoginHolder.getRequestHolder()));
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(0);

        //保存日志
        sysLogMapper.insert(sysLogWithBLOBs);

    }

    @Override
    public void saveRoleLog(SysRole before, SysRole after) {

        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();

        sysLogWithBLOBs.setType( LogType.TYPE_ROLE.getType() );
        sysLogWithBLOBs.setTargetId( after==null?before.getId():after.getId() );//增加时before没有id，删除时after没有id
        sysLogWithBLOBs.setOldValue( before==null?"": JsonMapper.obj2String(before));//增加时没有老数据
        sysLogWithBLOBs.setNewValue( after==null?"": JsonMapper.obj2String(after));//删除时没有新数据
        sysLogWithBLOBs.setOperator( LoginHolder.getUserHolder().getUsername());
        sysLogWithBLOBs.setOperateIp(IpUtil.getUserIP(LoginHolder.getRequestHolder()));
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(0);

        //保存日志
        sysLogMapper.insert(sysLogWithBLOBs);

    }

    @Override
    public void saveRoleAclLog(Integer roleId, List<Integer> before, List<Integer> after) {

        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();

        sysLogWithBLOBs.setType( LogType.TYPE_ROLE_ACL.getType() );
        sysLogWithBLOBs.setTargetId( roleId );
        sysLogWithBLOBs.setOldValue( before==null?"": JsonMapper.obj2String(before));//增加时没有老数据
        sysLogWithBLOBs.setNewValue( after==null?"": JsonMapper.obj2String(after));//删除时没有新数据
        sysLogWithBLOBs.setOperator( LoginHolder.getUserHolder().getUsername());
        sysLogWithBLOBs.setOperateIp(IpUtil.getUserIP(LoginHolder.getRequestHolder()));
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(0);

        //保存日志
        sysLogMapper.insert(sysLogWithBLOBs);
    }

    @Override
    public void saveRoleUserLog(Integer roleId, List<Integer> before, List<Integer> after) {

        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();

        sysLogWithBLOBs.setType( LogType.TYPE_ROLE_USER.getType() );
        sysLogWithBLOBs.setTargetId( roleId );
        sysLogWithBLOBs.setOldValue( before==null?"": JsonMapper.obj2String(before));//增加时没有老数据
        sysLogWithBLOBs.setNewValue( after==null?"": JsonMapper.obj2String(after));//删除时没有新数据
        sysLogWithBLOBs.setOperator( LoginHolder.getUserHolder().getUsername());
        sysLogWithBLOBs.setOperateIp(IpUtil.getUserIP(LoginHolder.getRequestHolder()));
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(0);

        //保存日志
        sysLogMapper.insert(sysLogWithBLOBs);
    }

    @Override
    public void recover(int id) {

        SysLogWithBLOBs log = sysLogMapper.selectByPrimaryKey(id);
        if (log == null){
            throw new ParamException("待还原的操作不存在！");
        }
        switch (LogType.getByValue(log.getType())){

            case TYPE_DEPT:
                recoverDept(log);
                break;
            case TYPE_USER:
                recoverUser(log);
                break;
            case TYPE_ACL_MODULE:
                recoverAclModule(log);
                break;
            case TYPE_ACL:
                recoverAcl(log);
                break;
            case TYPE_ROLE:
                recoverRole(log);
                break;
            case TYPE_ROLE_ACL:
                recoverRoleAcl(log);
                break;
            case TYPE_ROLE_USER:
                recoverRoleUser(log);
                break;
            default:
                throw new ParamException("还原发生未知的错误！！");
        }


    }

    private void recoverRoleUser(SysLogWithBLOBs log) {

        SysRole sysRole = sysRoleMapper.selectByPrimaryKey(log.getTargetId());
        if (sysRole == null){
            throw new ParamException("待还原的角色不存在！");
        }
        //json字符串有左右括号 手动去除
        String json = JsonMapper.string2Obj(log.getOldValue(), new TypeReference<String>() {});
        String userIds = json.substring(1).replaceAll("]", "");
        sysRoleService.changeUsers(sysRole.getId(),userIds);
        //将操作后的日志状态设置为1
        log.setStatus(1);
        sysLogMapper.updateByPrimaryKey(log);

    }

    private void recoverRole(SysLogWithBLOBs log) {

        SysRole sysRole = sysRoleMapper.selectByPrimaryKey(log.getTargetId());
        if (sysRole == null){
            throw new ParamException("待修改的角色不存在！");
        }
        if (StringUtils.isBlank(log.getOldValue()) || StringUtils.isBlank(log.getNewValue())){//说明是删除或者新增操作
            throw new ParamException("该操作不能还原！");
        }else {//角色的更新操作
            //通过oldValue获取以前的对象（要更新成的对象）
            SysRole sysRole1 = JsonMapper.string2Obj(log.getOldValue(), new TypeReference<SysRole>() {});
            RoleParam roleParam = new RoleParam();
            BeanUtils.copyProperties(sysRole1,roleParam);
            sysRoleService.update(roleParam);
        }
        //将操作后的日志状态设置为1
        log.setStatus(1);
        sysLogMapper.updateByPrimaryKey(log);
    }

    private void recoverAcl(SysLogWithBLOBs log) {

        SysAcl sysAcl = sysAclMapper.selectByPrimaryKey(log.getTargetId());
        if (sysAcl == null){
            throw new ParamException("待还原的权限点不存在！");
        }
        //通过oldValue获取以前的权限点对象
        SysAcl sysAcl1 = JsonMapper.string2Obj(log.getOldValue(), new TypeReference<SysAcl>() {});
        AclParam aclParam = new AclParam();
        BeanUtils.copyProperties(sysAcl1,aclParam);


        if (StringUtils.isBlank(log.getOldValue())){//新增

            sysAclService.delete(log.getTargetId());

        }else if (StringUtils.isBlank(log.getNewValue())){//删除

            sysAclService.save(aclParam);

        }else {//更新操作

            sysAclService.update(aclParam);

        }
        //将操作后的日志状态设置为1
        log.setStatus(1);
        sysLogMapper.updateByPrimaryKey(log);
    }

    private void recoverAclModule(SysLogWithBLOBs log) {

        SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(log.getTargetId());
        if (sysAclModule == null){
            throw new ParamException("待还原的权限模块不存在！");
        }
        //通过oldValue获取以前的对象
        SysAclModule sysAclModule1 = JsonMapper.string2Obj(log.getOldValue(), new TypeReference<SysAclModule>() {});
        AclModuleParam aclModuleParam = new AclModuleParam();

        if (StringUtils.isBlank(log.getOldValue())){//新增

            sysAclModuleService.delete(log.getTargetId());

        }else if (StringUtils.isBlank(log.getNewValue())){//删除

            BeanUtils.copyProperties(sysAclModule1,aclModuleParam);
            sysAclModuleService.save(aclModuleParam);

        }else {//更新操作
            //通过newValue获取目前的对象
            SysAclModule sysAclModule2 = JsonMapper.string2Obj(log.getNewValue(), new TypeReference<SysAclModule>() {});
            sysAclModuleService.updateWithChild(sysAclModule2,sysAclModule1);

        }
        //将操作后的日志状态设置为1
        log.setStatus(1);
        sysLogMapper.updateByPrimaryKey(log);
    }

    private void recoverUser(SysLogWithBLOBs log) {

        SysUser sysUser = sysUserMapper.selectByPrimaryKey(log.getTargetId());
        if (sysUser == null){
            throw new ParamException("待还原的用户不存在！");
        }
        //通过oldValue获取以前的对象
        SysUser sysUser1 = JsonMapper.string2Obj(log.getOldValue(), new TypeReference<SysUser>() {});
        UserParam userParam = new UserParam();
        BeanUtils.copyProperties(sysUser1,userParam);

        if (StringUtils.isBlank(log.getNewValue())){//说明之前是删除操作

            sysUserService.save(userParam);

        }else {//其它任何操作 (删除只改变状态 不删除数据)

            sysUserService.update(userParam);

        }
        //将操作后的日志状态设置为1
        log.setStatus(1);
        sysLogMapper.updateByPrimaryKey(log);

    }

    private void recoverRoleAcl(SysLogWithBLOBs log) {

        SysRole sysRole = sysRoleMapper.selectByPrimaryKey(log.getTargetId());
        if (sysRole == null){
            throw new ParamException("待还原的角色不存在！");
        }
        //json字符串有左右括号 手动去除
        String json = JsonMapper.string2Obj(log.getOldValue(), new TypeReference<String>() {});
        String aclIds = json.substring(1).replaceAll("]", "");
        sysRoleService.changeAcls(sysRole.getId(),aclIds);
        //将操作后的日志状态设置为1
        log.setStatus(1);
        sysLogMapper.updateByPrimaryKey(log);
    }

    private void recoverDept(SysLogWithBLOBs log) {

/*        //该操作已经还原过
        if (log.getStatus() == 1){
            throw new ParamException("该操作已经还原过");
        }*/

        SysDept sysDept1 = sysDeptMapper.selectByPrimaryKey(log.getTargetId());
        if (sysDept1 == null){
            throw new ParamException("待还原的部门不存在！");
        }

        //使用deptService中写好的操作
        if (StringUtils.isBlank(log.getOldValue())){//没有oldValue说明是新增操作 还原就是删除

            sysDeptService.delete(log.getTargetId());

        }else if(StringUtils.isBlank(log.getNewValue())){ //没有newValue说明是删除操作 还原就是新增

            //通过oldValue获取以前的值
            SysDept sysDept = JsonMapper.string2Obj(log.getOldValue(), new TypeReference<SysDept>() {});
            DeptParam deptParam = new DeptParam();
            BeanUtils.copyProperties(sysDept,deptParam);
            sysDeptService.save(deptParam);

        }else {//更新操作

            //通过log中的targetId获取目前的部门对象（还原前）
            SysDept before = sysDeptMapper.selectByPrimaryKey(log.getTargetId());
            //通过log中的oldValue获取以前的部门对象（还原后）
            SysDept after = JsonMapper.string2Obj(log.getOldValue(), new TypeReference<SysDept>() {});
            //更新过程中，层级可能发生改变，还原的时候子部门也要还原
            sysDeptService.updateWithChild(before,after);
            saveDeptLog(before,after);
        }
        //将操作后的日志状态设置为1
        log.setStatus(1);
        sysLogMapper.updateByPrimaryKey(log);
    }

}
