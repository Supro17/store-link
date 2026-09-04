package com.ojbk.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "分页查询参数")
public class PageDTO {


    @NotNull
    @Min(value = 1,message = "最小值为1")
    @Schema(description = "当前页码", example = "1")
    private Integer page = 1;

    @NotNull
    @Min(value = 1,message = "最小值为1")
    @Max(value = 100,message = "最大值为100")
    @Schema(description = "每页条数", example = "10")
    private Integer size = 10;

    @Schema(description = "状态筛选", example = "active")
    private String status;

    @Schema(description = "关键字搜索", example = "北京")
    private String keyword;


}