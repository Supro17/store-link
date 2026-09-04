package com.ojbk.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_storelink_transferstock")
@Schema(name = "TransferStock" ,description = "库存调拨")
public class TransferStock {

    @Schema(name = "tsId", title = "调拨ID", description = "库存调拨记录唯一标识，系统自动生成", requiredMode = Schema.RequiredMode.REQUIRED, example = "6001")
    @TableId(type= IdType.AUTO)
    private Long tsId;

    @Schema(name = "fromStoreId", title = "调出门店ID", description = "库存调出的源门店ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    private Long fromStoreId;

    @Schema(name = "toStoreId", title = "调入门店ID", description = "库存调入的目标门店ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1002")
    private Long toStoreId;

    @Schema(name = "sku", title = "商品SKU", description = "调拨商品的SKU编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "9001")
    private Long sku;

    @Schema(name = "createdAt", title = "创建时间", description = "记录创建时间", example = "2024-01-01 10:00:00")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime createdAt;

    @Schema(name = "updatedAt", title = "更新时间", description = "记录最后更新时间", example = "2024-01-01 10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @Schema(name = "Deleted", title = "逻辑删除", description = "软删除标记：0-正常，1-已删除", hidden = true)
    @TableLogic
    private Integer deleted;
}