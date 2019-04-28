package com.oyf.dao;

import com.oyf.model.SysRoleAcl;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface SysRoleAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleAcl record);

    int insertSelective(SysRoleAcl record);

    SysRoleAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleAcl record);

    int updateByPrimaryKey(SysRoleAcl record);

    List<Integer> findAclIdsByRoleIds(@Param("roleIds") List<Integer> roleIds);

    void deleteByRoleId(@Param("roleId") Integer roleId);

    void batchInsert(@Param("sysRoleAcls") ArrayList<SysRoleAcl> sysRoleAcls);
}