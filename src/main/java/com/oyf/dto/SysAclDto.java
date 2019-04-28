package com.oyf.dto;

import com.oyf.model.SysAcl;
import lombok.*;
import org.springframework.beans.BeanUtils;

/**
 * Create Time: 2019年03月27日 11:00
 * Create Author: 欧阳飞
 **/

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SysAclDto extends SysAcl {

    //权限点前打勾勾与否
    private boolean checked = false;

    //有没有操作的权限
    private boolean hasAcl = false;

    //将acl中字段封装进aclDto
    public static SysAclDto adpater(SysAcl sysAcl){
        SysAclDto sysAclDto = new SysAclDto();
        BeanUtils.copyProperties(sysAcl,sysAclDto);
        return sysAclDto;
    }




}
