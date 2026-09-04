package com.ojbk.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更新审核记录请求参数")
public class StoreAuditUpdateDTO {

    @Schema(description = "门店ID", example = "1001")
    private Long storeId;

    @Schema(description = "问题描述", example = "门店卫生不达标")
    private String issue;

    @Schema(description = "评分", example = "80")
    private Long score;
}