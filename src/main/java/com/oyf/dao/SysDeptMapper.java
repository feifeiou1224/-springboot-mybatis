package com.oyf.dao;

import com.oyf.model.SysDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysDeptMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    int selectDeptIfExist(@Param("parentId") Integer parentId, @Param("name") String name);

    List<SysDept> findAll();

    List<SysDept> selectByLevel(@Param("level") String level);
}