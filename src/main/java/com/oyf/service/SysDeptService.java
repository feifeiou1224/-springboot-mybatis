package com.oyf.service;


import com.oyf.model.SysDept;
import com.oyf.param.DeptParam;

public interface SysDeptService {

    void save(DeptParam deptParam);

    void update(DeptParam deptParam);

    void delete(Integer id);

    void updateWithChild(SysDept before, SysDept after);

}
