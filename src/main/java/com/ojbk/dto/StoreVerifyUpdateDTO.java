package com.ojbk.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更新核验记录请求参数")
public class StoreVerifyUpdateDTO {

    @Schema(description = "门店ID", example = "1001")
    private Long storeId;

    @Schema(description = "核验编码", example = "V2024001")
    private String code;

    @Schema(description = "核验结果(1:通过 0:未通过)", example = "1")
    private Integer ok;
}