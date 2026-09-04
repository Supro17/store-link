package com.ojbk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "督导巡店明细")
public class SupervisorAuditItemVO {
    @Schema(description = "门店id")
    private Long storeId;
    @Schema(description = "门店评分")
    private Long score;
    @Schema(description = "发现的问题")
    private String issue;



}
