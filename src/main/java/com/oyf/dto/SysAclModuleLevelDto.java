package com.oyf.dto;

import com.oyf.model.SysAclModule;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Create Time: 2019年03月23日 15:54
 * Create Author: 欧阳飞
 **/

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SysAclModuleLevelDto extends SysAclModule {

    //存储子层级的权限模块
    List<SysAclModuleLevelDto> aclModuleList = new ArrayList<>();

    //存储权限模块下的权限点
    List<SysAclDto> aclList = new ArrayList<>();

    //将aclmodule中的字段封装进dto
    public static SysAclModuleLevelDto adapter(SysAclModule sysAclModule){
        SysAclModuleLevelDto sysAclModuleLevelDto = new SysAclModuleLevelDto();
        BeanUtils.copyProperties(sysAclModule,sysAclModuleLevelDto);
        return sysAclModuleLevelDto;
    }




}
