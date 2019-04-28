package com.oyf.service.impl;

import com.oyf.beans.PageBean;
import com.oyf.dao.SysAclMapper;
import com.oyf.exception.ParamException;
import com.oyf.model.SysAcl;
import com.oyf.param.AclParam;
import com.oyf.service.SysAclService;
import com.oyf.service.SysLogService;
import com.oyf.utils.BeanValidator;
import com.oyf.utils.IpUtil;
import com.oyf.utils.LoginHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Create Time: 2019年03月25日 10:58
 * Create Author: 欧阳飞
 **/

@Service("sysAclService")
public class SysAclServiceImpl implements SysAclService {

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysLogService sysLogService;

    @Override
    public PageBean<SysAcl> getPageBeanById(Integer aclModuleId, PageBean<SysAcl> pageBean) {

        BeanValidator.check(pageBean);

        //查询模块下的权限点个数 = 总数据条数
        int count = sysAclMapper.selectIfExistByModuleId(aclModuleId);
        if (count > 0){//权限模块下还有权限点
            //通过模块id获取权限点集合
            List<SysAcl> sysAclList = sysAclMapper.findAllByAclModuleId(aclModuleId,pageBean);
            //新建一个分页对象
            PageBean<SysAcl> sysAclPageBean = new PageBean<>();
            sysAclPageBean.setTotal(count);//设置总数据条数
            sysAclPageBean.setData(sysAclList);//分页查询结果
            return sysAclPageBean;
        }
        return new PageBean<>();

    }

    @Override
    public void save(AclParam aclParam) {

        BeanValidator.check(aclParam);
        //权限名称 url 不能和其它权限点重复
        int nameCount = sysAclMapper.selectIfExistByName(aclParam.getName());
        int urlCount = sysAclMapper.selectIfExistByUrl(aclParam.getUrl());
        if (nameCount > 0 ){
            throw new ParamException("添加失败！权限名称重复！");
        }
        if (urlCount > 0){
            throw new ParamException("添加失败!URL重复！");
        }
        //设置code
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmSS");
        String dateCode = format.format(date);
        //封装数据
        SysAcl sysAcl = SysAcl.builder().name(aclParam.getName()).remark(aclParam.getRemark()).url(aclParam.getUrl()).aclModuleId(aclParam.getAclModuleId())
                .status(aclParam.getStatus()).seq(aclParam.getSeq()).operateTime(date).type(aclParam.getType()).build();
        sysAcl.setOperateIp(IpUtil.getUserIP(LoginHolder.getRequestHolder()));
        sysAcl.setOperator(LoginHolder.getUserHolder().getUsername());
        sysAcl.setCode(dateCode+"_"+new Random().nextInt(100));

        try {
            sysAclMapper.insert(sysAcl);
        }catch (Exception e){
            throw new ParamException("添加权限点时发生未知错误！");
        }
        sysLogService.saveAclLog(null,sysAcl);
    }

    @Override
    public void update(AclParam aclParam) {

        BeanValidator.check(aclParam);

        //通过id获取修改前的数据
        SysAcl beforeAcl = sysAclMapper.selectByPrimaryKey(aclParam.getId());
        //修改前后的name不一样，就判断是否重复
        if ( !beforeAcl.getName().equals(aclParam.getName())){
            if (sysAclMapper.selectIfExistByName(aclParam.getName()) > 0){
                throw new ParamException("修改失败！权限名称不能相同！");
            }
        }
        //修改前后的url不一样，就判断是否重复
        if ( !beforeAcl.getUrl().equals(aclParam.getUrl())){
            if (sysAclMapper.selectIfExistByUrl(aclParam.getUrl()) > 0){
                throw new ParamException("修改失败！权限url不能相同！");
            }
        }
        //封装数据
        SysAcl sysAcl = SysAcl.builder().aclModuleId(aclParam.getAclModuleId()).name(aclParam.getName()).type(aclParam.getType())
                .url(aclParam.getUrl()).seq(aclParam.getSeq()).remark(aclParam.getRemark()).status(aclParam.getStatus()).build();
        sysAcl.setOperateIp(IpUtil.getUserIP(LoginHolder.getRequestHolder()));
        sysAcl.setOperator(LoginHolder.getUserHolder().getUsername());
        sysAcl.setOperateTime(new Date());
        //更新
        try {
            sysAclMapper.updateByPrimaryKeySelective(sysAcl);
        }catch (Exception e){
            throw new ParamException("更新失败！发生未知错误！");
        }
        sysLogService.saveAclLog(beforeAcl,sysAcl);
    }

    @Override
    public void delete(Integer id) {
        if (id == null){
            throw new ParamException("待删除的权限点不存在！");
        }
        try {
            sysLogService.saveAclLog(sysAclMapper.selectByPrimaryKey(id),null);
            sysAclMapper.deleteByPrimaryKey(id);
        }catch (Exception e){
            throw new ParamException("删除权限点时发生未知错误！");
        }

    }

}
