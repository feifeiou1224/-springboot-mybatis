package com.oyf.beans;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

/**
 * Create Time: 2019年03月23日 10:41
 * Create Author: 欧阳飞
 **/

public class PageBean<T> {

    @Getter
    @Setter
    @Min(value = 0,message = "当前页码不合法")
    private int pageNo = 1;//页码

    @Getter
    @Setter
    @Min(value = 0,message = "每页条数不合法")
    private int pageSize = 10;//每页的条数

    @Getter
    @Setter
    private int offset = 1;//偏移量：每次从数据库中第几条开始查询(pageNo-1)*pageSize

    @Getter
    @Setter
    private int total = 0;//数据总条数

    @Getter
    @Setter
    private List<T> data = new ArrayList<>();

    public int getOffset() {
        return (pageNo-1)*pageSize;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
