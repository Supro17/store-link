package com.ojbk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ojbk.common.Result;
import com.ojbk.service.DashboardService;
import com.ojbk.vo.DashboardVO;
import com.ojbk.vo.StoreRankingVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
@Tag(name = "仪表盘")
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    @GetMapping
    @SaCheckPermission("storeline:dash")
    @Operation(summary = "获取仪表盘数据", description = "返回门店总数、营收、核销、巡店等统计指标。ADMIN/HQ看全部，店长/导购/督导只看自己门店")
    public Result<DashboardVO> resultDashboard(){
        return Result.success(dashboardService.getDashboardVO());
    }



    @GetMapping("/ranking")
    @SaCheckPermission("storeline:dash")
    public Result<List<StoreRankingVO>> storeRanking(){
        return Result.success(dashboardService.getStoreRanking());
    }
}