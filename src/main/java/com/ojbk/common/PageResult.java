package com.ojbk.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Data
@Schema(description = "分页查询结果")
public class PageResult<T> {

    @Schema(description = "数据列表")
    private List<T> list;

    @Schema(description = "总记录数", example = "100")
    private Long total;

    @Schema(description = "当前页码", example = "1")
    private Long page;

    @Schema(description = "每页条数", example = "10")
    private Long size;


}