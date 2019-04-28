package com.oyf.service.impl;

import com.oyf.beans.PageBean;
import com.oyf.dao.SysUserMapper;
import com.oyf.exception.ParamException;
import com.oyf.model.SysUser;
import com.oyf.param.UserLoginParam;
import com.oyf.param.UserParam;
import com.oyf.service.SysLogService;
import com.oyf.service.SysUserService;
import com.oyf.utils.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Create Time: 2019年03月22日 10:25
 * Create Author: 欧阳飞
 **/

@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper mapper;

    @Resource
    private SysLogService sysLogService;

    @Override
    public SysUser findOne(UserLoginParam userParam) {

        BeanValidator.check(userParam);

        //获取到的数据
        String username = userParam.getUsername();
        String password = userParam.getPassword();
        //加密密码
        String Md5Password = MD5Util.encrypt(password);

        //通过username查询出一个user对象
        SysUser sysUser = mapper.findOne(username);

        if (sysUser == null){
            throw new ParamException("用户不存在！");
        }else if(!sysUser.getPassword().equals(Md5Password)){
            throw new ParamException("密码有误！");
        }else if (sysUser.getStatus() != 1){
            throw new ParamException("用户状态异常！请联系管理员！");
        }

        return sysUser;
    }

    @Override
    public void save(UserParam userParam) {

        BeanValidator.check(userParam);

        int count = mapper.selectIfExistByUsername(userParam.getUsername());
        if (count > 0){//用户名重复
            throw new ParamException("添加失败！重复用户！");
        }
        int emailCount = mapper.selectIfExistByEmail(userParam.getMail());
        if (emailCount > 0){
            throw new ParamException("添加失败！邮箱重复！");
        }
        //获取初始密码
        String initPassword = userParam.getPassword();
        //封装数据到user对象
        SysUser sysUser = SysUser.builder().username(userParam.getUsername()).password(MD5Util.encrypt(initPassword)).telephone(userParam.getTelephone())
                .mail(userParam.getMail()).deptId(userParam.getDeptId()).status(userParam.getStatus()).remark(userParam.getRemark()).build();
        sysUser.setOperateIp(IpUtil.getUserIP(LoginHolder.getRequestHolder()));
        sysUser.setOperator(LoginHolder.getUserHolder().getUsername());
        sysUser.setOperateTime(new Date());
        try {
            //发送邮件
            MailUtils.sendMail(sysUser.getMail(),"来自权限管理系统的注册提示，您的初始密码是："+initPassword,"权限管理系统");
        }catch (Exception e){
            throw new ParamException("发送邮件失败！请检查邮箱格式！");
        }
        //添加
        mapper.insert(sysUser);
        sysLogService.saveUserLog(null,sysUser);
    }

    @Override
    public void update(UserParam userParam) {

        BeanValidator.check(userParam);

        Integer id = userParam.getId();
        //修改前的用户
        SysUser before = mapper.selectByPrimaryKey(id);
        String beforeUsername = before.getUsername();
        String beforeMail = before.getMail();

        //修改前后用户名不一样，则判断用户名重复问题
        if (!beforeUsername.equalsIgnoreCase(userParam.getUsername())){
            int count = mapper.selectIfExistByUsername(userParam.getUsername());
            if (count > 0){//用户名重复
                throw new ParamException("修改失败！用户名重复！");
            }
        }
        //修改前后邮箱不一样，则判断邮箱重复问题
        if (!beforeMail.equalsIgnoreCase(userParam.getMail())){
            int emailCount = mapper.selectIfExistByEmail(userParam.getMail());
            if (emailCount > 0){
                throw new ParamException("修改失败！邮箱重复！");
            }
        }
        //封装数据
        SysUser sysUser = SysUser.builder().id(userParam.getId()).deptId(userParam.getDeptId()).username(userParam.getUsername()).mail(userParam.getMail())
        .telephone(userParam.getTelephone()).status(userParam.getStatus()).remark(userParam.getRemark()).operateTime(new Date())
        .operateIp(IpUtil.getUserIP(LoginHolder.getRequestHolder())).operator(LoginHolder.getUserHolder().getUsername()).build();
        //更新
        try {
            mapper.updateByPrimaryKeySelective(sysUser);
        }catch (Exception e){
            throw new ParamException("修改时发生未知错误！");
        }
        sysLogService.saveUserLog(before,sysUser);
    }

    @Override
    public PageBean<SysUser> getPageBeanById(Integer deptId, PageBean<SysUser> pageBean) {

        BeanValidator.check(pageBean);

        //查看部门下是否有用户
        int count = mapper.checkIfExistByDeptId(deptId);
        if (count > 0){
            //新创建一个分页对象
            PageBean<SysUser> sysUserPageBean = new PageBean<>();
            //通过部门id获取用户集合
            List<SysUser> users = mapper.findByDeptId(deptId,pageBean);
            sysUserPageBean.setData(users);
            sysUserPageBean.setTotal(count);

            return sysUserPageBean;
        }

        return new PageBean<>();
    }
}
