package com.oyf.service;

import com.oyf.model.SysAclModule;
import com.oyf.param.AclModuleParam;

public interface SysAclModuleService {

    void update(AclModuleParam aclModuleParam);

    void save(AclModuleParam aclModuleParam);

    void delete(Integer id);

    void updateWithChild(SysAclModule beforeModule, SysAclModule afterModule);

}
