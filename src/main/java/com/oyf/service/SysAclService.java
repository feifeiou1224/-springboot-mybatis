package com.oyf.service;

import com.oyf.beans.PageBean;
import com.oyf.model.SysAcl;
import com.oyf.param.AclParam;

public interface SysAclService {

    PageBean<SysAcl> getPageBeanById(Integer aclModuleId, PageBean<SysAcl> pageBean);

    void save(AclParam aclParam);

    void update(AclParam aclParam);

    void delete(Integer id);

}
