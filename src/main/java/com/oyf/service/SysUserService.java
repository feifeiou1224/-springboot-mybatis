package com.oyf.service;

import com.oyf.beans.PageBean;
import com.oyf.model.SysUser;
import com.oyf.param.UserLoginParam;
import com.oyf.param.UserParam;

public interface SysUserService {

    SysUser findOne(UserLoginParam userParam);

    void save(UserParam userParam);

    void update(UserParam userParam);

    PageBean<SysUser> getPageBeanById(Integer deptId, PageBean<SysUser> pageBean);

}
