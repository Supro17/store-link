package com.ojbk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "销售辅助记录信息")
public class SalesAssistVO {

    @NotNull
    @Schema(description = "销售辅助记录ID", example = "1")
    private Long saId;

    @NotNull
    @Schema(description = "门店ID", example = "1001")
    private Long storeId;

    @NotBlank
    @Schema(description = "名称", example = "门店名")
    private String name;

    @NotBlank
    @Schema(description = "销售额", example = "50000.00")
    private BigDecimal sales;

}