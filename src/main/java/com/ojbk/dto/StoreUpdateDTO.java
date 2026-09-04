package com.ojbk.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "更新门店请求参数")
public class StoreUpdateDTO {

    @NotBlank
    @Schema(description = "门店名称", example = "北京旗舰店")
    private String name;

    @NotBlank
    @Schema(description = "所在城市", example = "北京")
    private String city;

    @NotBlank
    @Schema(description = "门店状态", example = "active")
    private String status;
}