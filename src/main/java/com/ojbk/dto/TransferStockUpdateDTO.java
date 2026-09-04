package com.ojbk.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更新调拨记录请求参数")
public class TransferStockUpdateDTO {

    @Schema(description = "调出门店ID", example = "1001")
    private Long fromStoreId;

    @Schema(description = "调入门店ID", example = "1002")
    private Long toStoreId;

    @Schema(description = "SKU编号", example = "2001")
    private Long sku;
}