package com.oyf.param;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Create Time: 2019年03月22日 20:29
 * Create Author: 欧阳飞
 **/

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginParam {

    @NotBlank(message = "用户名不能为空")
    @Length(max = 15,message = "用户名长度在15个字以内")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Length(max = 15,message = "密码长度在15个字以内")
    private String password;

}
