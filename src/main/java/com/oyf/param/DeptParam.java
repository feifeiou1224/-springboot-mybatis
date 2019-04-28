package com.oyf.param;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;


/**
 * Create Time: 2019年03月21日 15:59
 * Create Author: 欧阳飞
 **/

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeptParam {

    private Integer id;

    @NotBlank(message = "部门名称不能为空")
    @Length(min = 2,max = 15,message = "部门名称字数需要在2-15之间")
    private String name;

    private Integer parentId = 0;//如果不选择，默认为0，是最上级部门

    @NotNull
    private Integer seq;

    @Length(max = 150,message = "部门备注字数不能超过150")
    private String remark;


}
