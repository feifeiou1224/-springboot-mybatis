package com.oyf.param;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;


/**
 * Create Time: 2019年03月25日 10:51
 * Create Author: 欧阳飞
 **/

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AclParam {

    private Integer id;

    @NotBlank
    @Length(min = 2,max = 20,message = "权限点名字长度需要在2-20个字之间")
    private String name;

    @NotNull(message = "权限模块id不能为空")
    private Integer aclModuleId;

    @NotNull(message = "seq不能为空")
    private Integer seq;

    @Length(max = 150,message = "备注长度在150个字以内")
    private String remark;

    private Integer status;

    @Length(min = 6,max = 100,message = "url长度在6-100个字符之间")
    private String url;

    private Integer type;

}
