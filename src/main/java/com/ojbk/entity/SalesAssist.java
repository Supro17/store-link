package com.ojbk.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_storelink_salesassist")
@Schema(name = "SalesAssist" ,description = "导购管理")
public class SalesAssist {

    @Schema(name = "saId", title = "导购ID", description = "导购人员唯一标识，系统自动生成", requiredMode = Schema.RequiredMode.REQUIRED, example = "2001")
    @TableId(type= IdType.AUTO)
    private Long saId;

    @Schema(name = "storeId", title = "门店ID", description = "关联的门店唯一标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    private Long storeId;

    @Schema(name = "name", title = "导购姓名", description = "导购人员的姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    private String name;

    @Schema(name = "sales", title = "销售业绩", description = "导购的销售业绩金额", example = "56800.00")
    private BigDecimal sales;

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

    @Version
    @Schema(name = "version", title = "乐观锁版本号", description = "用于乐观锁控制，更新时自动递增", example = "0")
    private Long version;


}