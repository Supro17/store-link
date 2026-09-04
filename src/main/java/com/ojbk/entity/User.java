package com.ojbk.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_storelink_system_user")
@Schema(name = "User" ,description = "用户")
public class User {

    @Schema(name = "userId", title = "用户ID", description = "用户唯一标识，系统自动生成", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @TableId(type= IdType.AUTO)
    private Long userId;

    @Schema(name = "userName", title = "登录名", description = "用户登录账号", requiredMode = Schema.RequiredMode.REQUIRED, example = "admin")
    private String userName;

    @Schema(name = "password", title = "密码", description = "Bcrypt加密后的密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "******")
//    @TableField(select = false)
    private String password;

    @Schema(name = "realName", title = "姓名", description = "用户真实姓名", example = "张三")
    private String realName;

    @Schema(name = "phone", title = "手机号", description = "用户手机号码", example = "13800138000")
    private String phone;

    @Schema(name = "storeId", title = "所属门店ID", description = "店长/导购所属门店ID，总部/督导为空", example = "1001")
    private Long storeId;

    @Schema(name = "status", title = "状态", description = "用户状态：1-启用，0-停用", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(name = "version", title = "乐观锁版本号", description = "用于乐观锁控制，更新时自动递增", example = "0")
    @Version
    private Integer version;

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