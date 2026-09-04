package com.ojbk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("t_storelink_system_user_role")
@Schema(name = "User_Role" ,description = "用户与角色关联")
public class UserRole {

    @Schema(name = "id", title = "关联ID", description = "用户角色关联记录唯一标识，系统自动生成", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @TableId(type= IdType.AUTO)
    private Long id;

    @Schema(name = "userId", title = "用户ID", description = "关联的用户唯一标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long userId;

    @Schema(name = "roleId", title = "角色ID", description = "关联的角色唯一标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long roleId;

}