package com.oyf.dao;

import com.oyf.beans.PageBean;
import com.oyf.model.SysAcl;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysAclMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(SysAcl record);

    int insertSelective(SysAcl record);

    SysAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAcl record);

    int updateByPrimaryKey(SysAcl record);

    int selectIfExistByModuleId(@Param("aclModuleId") Integer aclModuleId);

    List<SysAcl> findAllByAclModuleId(@Param("aclModuleId") Integer aclModuleId, @Param("pageBean") PageBean<SysAcl> pageBean);

    int selectIfExistByName(@Param("name") String name);

    int selectIfExistByUrl(@Param("url") String url);

    List<SysAcl> findAll();

    List<SysAcl> findAllByAclIds(@Param("aclIds") List<Integer> aclIds);

    SysAcl findAclByUrl(@Param("url") String url);
}