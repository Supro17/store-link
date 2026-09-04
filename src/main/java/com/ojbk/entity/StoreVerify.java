package com.ojbk.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_storelink_storeverify")
@Schema(name = "StoreVerify" ,description = "到店核销")
public class StoreVerify {

    @Schema(name = "svId", title = "核销ID", description = "到店核销记录唯一标识，系统自动生成", requiredMode = Schema.RequiredMode.REQUIRED, example = "4001")
    @TableId(type= IdType.AUTO)
    private Long svId;

    @Schema(name = "storeId", title = "门店ID", description = "关联的门店唯一标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    private Long storeId;

    @Schema(name = "code", title = "核销码", description = "到店核销的验证码", example = "ABC123")
    private String code;

    @Schema(name = "ok", title = "核销状态", description = "核销是否成功：0-未核销，1-已核销", example = "1")
    private Integer ok;

    @Schema(name = "createdAt", title = "创建时间", description = "记录创建时间", example = "2024-01-01 10:00:00")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime createdAt;

    @Schema(name = "updatedAt", title = "更新时间", description = "记录最后更新时间", example = "2024-01-01 10:00:00")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime updatedAt;

    @Schema(name = "Deleted", title = "逻辑删除", description = "软删除标记：0-正常，1-已删除", hidden = true)
    @TableLogic
    private Integer deleted;



}