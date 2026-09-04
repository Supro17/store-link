package com.ojbk.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户请求参数")
public class UserDTO {

    @Schema(description = "用户ID(创建时不需要)", example = "1")
    private Long userId;

    @Schema(description = "门店ID", example = "1001")
    private Long storeId;

    @Schema(description = "登录名", example = "zhangsan")
    private String userName;

    @Schema(description = "密码(创建时必填，编辑时选填)", example = "123456")
    private String password;

    @Schema(description = "角色ID", example = "3")
    private Long roleId;

    @Schema(description = "状态(1:正常 0:禁用)", example = "1")
    private Integer status;

    @Schema(description = "真实姓名", example = "张三")
    private String realName;

    @Schema(description = "手机号", example = "13800138000")
    private String phone;
}