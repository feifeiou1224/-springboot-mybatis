package com.oyf.dto;

import com.oyf.model.SysDept;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Create Time: 2019年03月21日 17:33
 * Create Author: 欧阳飞
 **/

@Getter
@Setter
@ToString
public class SysDeptLevelDto extends SysDept {

    List<SysDeptLevelDto> deptList = new ArrayList<>();

    //将dept中的字段封装到deptdto中
    public static SysDeptLevelDto adapter(SysDept sysDept){
        SysDeptLevelDto sysDeptDto = new SysDeptLevelDto();
        BeanUtils.copyProperties(sysDept,sysDeptDto);
        return sysDeptDto;
    }
}
