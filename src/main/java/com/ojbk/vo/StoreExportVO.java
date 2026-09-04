package com.ojbk.vo;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data

public class StoreExportVO {

    @ExcelProperty("门店ID")
    private Long storeId;

    @ExcelProperty("门店名称")
    private String name;

    @ExcelProperty("所在城市")
    private String city;

    @ExcelProperty("营业状态")
    private String status;

    @ExcelProperty("创建时间")
    private String createdAt;


}
