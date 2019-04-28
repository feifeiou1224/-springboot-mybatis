package com.oyf.param;

import com.oyf.utils.PasswordUtil;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Create Time: 2019年03月22日 10:17
 * Create Author: 欧阳飞
 **/

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserParam {

    private Integer id;

    @NotBlank(message = "用户名不能为空")
    @Length(max = 15,message = "用户名长度在15个字符以内")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Length(max = 15,message = "密码长度在15个字符以内")
    private String password = PasswordUtil.randomPassword();

    @NotBlank(message = "电话不能为空")
    @Length(max = 13,message = "电话长度在13个数字以内")
    private String telephone;

    @NotBlank(message = "邮箱不能为空")
    @Length(max = 50,message = "邮箱长度在50个字符以内")
    private String mail;

    @NotNull(message = "必须提供用户所在部门")
    private Integer deptId;

    @NotNull(message = "必须提供用户的状态")
    private Integer status;

    @Length(max = 200,message = "备注长度不能超过200个字")
    private String remark;

}
