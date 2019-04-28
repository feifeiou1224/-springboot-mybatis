package com.oyf.param;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Create Time: 2019年03月25日 15:40
 * Create Author: 欧阳飞
 **/

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleParam {

    private Integer id;

    @NotBlank
    @Length(min = 2,max = 20,message = "角色名称长度在2-20之间")
    private String name;

    @Length(max = 150,message = "备注在150个字之内")
    private String remark;

    private Integer status;

    private Integer type;


}
