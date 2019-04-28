package com.oyf.dao;

import com.oyf.beans.PageBean;
import com.oyf.model.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser findOne(@Param("username") String username);

    int checkIfExistByDeptId(@Param("deptId") Integer id);

    int selectIfExistByUsername(@Param("username") String username);

    List<SysUser> findByDeptId(@Param("deptId") Integer deptId, @Param("pageBean") PageBean<SysUser> pageBean);

    int selectIfExistByEmail(@Param("mail") String mail);

    List<SysUser> findAll();

    List<SysUser> findByUserIds(@Param("userIds") List<Integer> userIds);
}