package com.ojbk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "用户信息")
public class UserVO {

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "角色编码", example = "STORE_MANAGER")
    private String roleCode;

    @Schema(description = "角色名称", example = "店长")
    private String roleName;

    @Schema(description = "登录名", example = "zhangsan")
    private String userName;

    @Schema(description = "门店ID", example = "1001")
    private Long storeId;

    @Schema(description = "状态(1:正常 0:禁用)", example = "1")
    private Integer status;

    @Schema(description = "真实姓名", example = "张三")
    private String realName;

    @Schema(description = "手机号", example = "13800138000")
    private String phone;


}