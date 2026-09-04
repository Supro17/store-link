package com.ojbk.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_storelink_storeperf")
@Schema(name = "StorePerf" ,description = "门店业绩")
public class StorePerf {

    @Schema(name = "spId", title = "业绩ID", description = "门店业绩记录唯一标识，系统自动生成", requiredMode = Schema.RequiredMode.REQUIRED, example = "5001")
    @TableId(type= IdType.AUTO)
    private Long spId;

    @Schema(name = "storeId", title = "门店ID", description = "关联的门店唯一标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    private Long storeId;

    @Schema(name = "revenue", title = "实际营收", description = "门店实际营收金额", example = "128500.00")
    private BigDecimal revenue;

    @Schema(name = "target", title = "目标营收", description = "门店目标营收金额", example = "150000.00")
    private BigDecimal target;

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