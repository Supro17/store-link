package com.ojbk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;



@Data
@Schema(description = "门店信息")
public class StoreVO {


    @Schema(description = "门店编号", example = "1001")
    private Long storeId;

    @Schema(description = "门店名称", example = "北京旗舰店")
    private String name;

    @Schema(description = "所在城市", example = "北京")
    private String city;

    @Schema(description = "门店状态", example = "active")
    private String status;


}