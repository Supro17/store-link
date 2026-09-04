package com.ojbk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "仪表盘统计数据")
public class DashboardVO {

    @Schema(description = "门店总数", example = "50")
    private Long totalStore;

    @Schema(description = "营业中门店数", example = "42")
    private Long activeStore;

    @Schema(description = "总营收", example = "5280000.00")
    private BigDecimal totalRevenue;

    @Schema(description = "总目标", example = "6000000.00")
    private BigDecimal totalTarget;

    @Schema(description = "达成率(%)", example = "88.0")
    private BigDecimal achievementRate;

    @Schema(description = "导购员总数", example = "120")
    private Long totalSalesAssist;

    @Schema(description = "核销总数", example = "3500")
    private Long totalVerify;

    @Schema(description = "已核销数", example = "3200")
    private Long successVerify;

    @Schema(description = "巡店次数", example = "85")
    private Long totalAudit;

    @Schema(description = "平均巡店评分", example = "87.5")
    private BigDecimal avgScore;

    @Schema(description = "调拨总数", example = "200")
    private Long totalTransfer;

    @Schema(description = "用户总数", example = "60")
    private Long totalUser;

    @Schema(description = "导购姓名（仅导购角色可见）", example = "张三")
    private String guideName;

    @Schema(description = "个人销售业绩（仅导购角色可见）", example = "56800.00")
    private BigDecimal guideSales;


    @Schema(description = "督导巡店明细")
    private List<SupervisorAuditItemVO> supervisorAuditItemVOS;

}