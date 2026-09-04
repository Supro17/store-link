package com.ojbk.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "创建调拨记录请求参数")
public class TransferStockDTO {

    @Schema(description = "调拨记录ID", example = "1")
    private Long tsId;

    @Schema(description = "调出门店ID", example = "1001")
    private Long fromStoreId;

    @Schema(description = "调入门店ID", example = "1002")
    private Long toStoreId;

    @Schema(description = "SKU编号", example = "2001")
    private Long sku;
}