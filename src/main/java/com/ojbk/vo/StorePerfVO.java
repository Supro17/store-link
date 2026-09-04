package com.ojbk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "绩效记录信息")
public class StorePerfVO {

    @Schema(description = "绩效记录ID", example = "1")
    private Long spId;

    @Schema(description = "门店ID", example = "1001")
    private Long storeId;
    @Schema(description = "门店名称")
    private String storeName;

    @Schema(description = "营收", example = "100000.00")
    private BigDecimal revenue;

    @Schema(description = "目标值", example = "120000.00")
    private BigDecimal target;
}