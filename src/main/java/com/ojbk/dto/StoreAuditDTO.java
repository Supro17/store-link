package com.ojbk.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "创建审核记录请求参数")
public class StoreAuditDTO {


    @Schema(description = "审核记录ID", example = "1")
    private Long saId;

    @Schema(name = "userId", title = "督导用户ID", description = "创建该巡店记录的督导用户ID，关联系统用户表", example = "5")
    private Long userId;

    @Schema(description = "门店ID", example = "1001")
    private Long storeId;

    @Schema(description = "问题描述", example = "门店卫生不达标")
    private String issue;

    @Schema(description = "评分", example = "80")
    private Long score;

}