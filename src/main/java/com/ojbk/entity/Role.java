package com.ojbk.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_storelink_system_role")
@Schema(name = "Role" ,description = "角色")
public class Role {

    @Schema(name = "roleId", title = "角色ID", description = "角色唯一标识，系统自动生成", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @TableId(type= IdType.AUTO)
    private Long roleId;

    @Schema(name = "roleCode", title = "角色编码", description = "角色的唯一编码，如ADMIN、STORE_MANAGER", requiredMode = Schema.RequiredMode.REQUIRED, example = "ADMIN")
    private String roleCode;

    @Schema(name = "roleName", title = "角色名称", description = "角色的显示名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "管理员")
    private String roleName;

    @Schema(name = "status", title = "状态", description = "角色状态：1-启用，0-停用", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(name = "Deleted", title = "逻辑删除", description = "软删除标记：0-正常，1-已删除", hidden = true)
    @TableLogic
    private Integer deleted;

    @Schema(name = "createdAt", title = "创建时间", description = "记录创建时间", example = "2024-01-01 10:00:00")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime createdAt;

    @Schema(name = "updatedAt", title = "更新时间", description = "记录最后更新时间", example = "2024-01-01 10:00:00")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime updatedAt;


}