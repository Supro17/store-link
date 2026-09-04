package com.ojbk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "登录响应结果")
public class LoginVO {

    @Schema(description = "认证令牌")
    private String token;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "真实姓名", example = "张三")
    private String realName;

    @Schema(description = "角色编码", example = "admin")
    private String roleCode;

    @Schema(description = "门店ID", example = "1001")
    private Long storeId;
}