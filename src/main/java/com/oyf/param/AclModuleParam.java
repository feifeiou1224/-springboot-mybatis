package com.oyf.param;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Create Time: 2019年03月23日 17:14
 * Create Author: 欧阳飞
 **/

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AclModuleParam {

    private Integer id;

    @NotBlank
    @Length(min = 2,max = 15,message = "名字长度在2-15个字之间")
    private String name;


    private Integer parentId;

    @NotNull
    private Integer seq;

    @Length(max = 150,message = "备注长度不能超过150个字")
    private String remark;

    private Integer status;

}
