package com.ojbk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "门店详情(含关联数据)")
public class StorePlusVO {
    @Schema(description = "门店编号", example = "1001")
    private Long storeId;

    @Schema(description = "门店名称", example = "北京旗舰店")
    private String name;

    @Schema(description = "所在城市", example = "北京")
    private String city;

    @Schema(description = "门店状态", example = "open")
    private String status;

    @Schema(description = "门店的导购")
    private List<SalesAssistVO> salesAssistVOList;

    @Schema(description = "门店的督导")
    private List<StoreAuditVO> storeAuditVOList;

    @Schema(description = "门店的业绩" )
    private List<StorePerfVO> storePerfVOList;




}