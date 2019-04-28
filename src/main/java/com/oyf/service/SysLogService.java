package com.oyf.service;

import com.oyf.beans.PageBean;
import com.oyf.model.*;
import com.oyf.param.LogParam;

import java.util.List;

public interface SysLogService {

    PageBean<SysLogWithBLOBs> getPageBean(LogParam logParam, PageBean<SysLogWithBLOBs> pageBean);

    //每次对数据有修改的操作都要记录
    void saveDeptLog(SysDept before, SysDept after);

    void saveUserLog(SysUser before, SysUser after);

    void saveAclModuleLog(SysAclModule before, SysAclModule after);

    void saveAclLog(SysAcl before, SysAcl after);

    void saveRoleLog(SysRole before, SysRole after);

    void saveRoleAclLog(Integer roleId, List<Integer> before, List<Integer> after);

    void saveRoleUserLog(Integer roleId, List<Integer> before, List<Integer> after);

    void recover(int id);

}
