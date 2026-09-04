package com.ojbk.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "创建销售辅助记录请求参数")
public class SalesAssistDTO {

    @Schema(description = "销售辅助记录ID", example = "1")
    private Long saId;

    @Schema(description = "门店ID", example = "1001")
    private Long storeId;

    @Schema(description = "名称", example = "促销活动A")
    private String name;

    @Schema(description = "销售额", example = "50000.00")
    private BigDecimal sales;





}