package com.ojbk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "审核记录信息")
public class StoreAuditVO {

    @Schema(description = "审核记录ID", example = "1")
    private Long saId;

    @Schema(description = "门店ID", example = "1001")
    private Long storeId;

    @Schema(description = "问题描述", example = "门店卫生不达标")
    private String issue;

    @Schema(description = "评分", example = "80")
    private Long score;
}