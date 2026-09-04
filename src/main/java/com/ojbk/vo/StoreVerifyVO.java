package com.ojbk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "核验记录信息")
public class StoreVerifyVO {

    @Schema(description = "核验记录ID", example = "1")
    private Long svId;

    @Schema(description = "门店ID", example = "1001")
    private Long storeId;

    @Schema(description = "核验编码", example = "V2024001")
    private String code;

    @Schema(description = "核验结果(1:通过 0:未通过)", example = "1")
    private Integer ok;


}