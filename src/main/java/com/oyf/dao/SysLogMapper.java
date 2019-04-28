package com.oyf.dao;

import com.oyf.beans.PageBean;
import com.oyf.dto.SysLogDto;
import com.oyf.model.SysLog;
import com.oyf.model.SysLogWithBLOBs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysLogWithBLOBs record);

    int insertSelective(SysLogWithBLOBs record);

    SysLogWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysLogWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(SysLogWithBLOBs record);

    int updateByPrimaryKey(SysLog record);

    int countByLogDto(@Param("dto") SysLogDto sysLogDto);

    List<SysLogWithBLOBs> getLogListByLogDto(@Param("dto") SysLogDto sysLogDto, @Param("pageBean") PageBean<SysLogWithBLOBs> pageBean);
}