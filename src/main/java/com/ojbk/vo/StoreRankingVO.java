package com.ojbk.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreRankingVO {

    private Long storeId;

    private String storeName;

    private BigDecimal revenue;

    private BigDecimal target;




}
