package com.ojbk.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "更新绩效记录请求参数")
public class StorePerfUpdateDTO {

    @Schema(description = "门店ID", example = "1001")
    private Long storeId;

    @Schema(description = "营收", example = "100000.00")
    private BigDecimal revenue;

    @Schema(description = "目标值", example = "120000.00")
    private BigDecimal target;
}