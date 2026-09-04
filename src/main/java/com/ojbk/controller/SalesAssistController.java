package com.ojbk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ojbk.common.PageResult;
import com.ojbk.common.Result;
import com.ojbk.dto.PageDTO;
import com.ojbk.dto.SalesAssistDTO;
import com.ojbk.dto.SalesAssistUpdateDTO;
import com.ojbk.service.SalesAssistService;
import com.ojbk.vo.SalesAssistVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/salesassist")
@Tag(name = "销售辅助管理")
public class SalesAssistController {

    @Resource
    private SalesAssistService salesAssistService;

    @GetMapping("/list")
    @SaCheckPermission("salesAssist:list")
    @Operation(summary = "销售辅助列表", description = "分页查询销售辅助记录列表")
    public Result<PageResult<SalesAssistVO>> salesAssistPage(@Valid PageDTO pageDTO){

        return  Result.success(salesAssistService.salesAssistlistPage(pageDTO));

    }

    @PostMapping("/create")
    @SaCheckPermission("salesAssist:create")
    @Operation(summary = "创建销售辅助记录", description = "创建销售辅助记录，需提供门店ID、名称和销售额")
    public Result<SalesAssistVO> salesAssistCreat(@Valid @RequestBody SalesAssistDTO assistDTO){

         return Result.success(salesAssistService.salesAssistCreate(assistDTO));

    }

    @GetMapping("/{id}")
    @SaCheckPermission("salesAssist:selectById")
    @Operation(summary = "根据ID查询销售辅助记录", description = "根据销售辅助记录ID查询详细信息")
    public Result<SalesAssistVO> selectSalesById(@Parameter(description = "销售辅助记录ID") @PathVariable Long id){

        SalesAssistVO salesAssistVO = salesAssistService.selectsalesAssistById(id);

        return Result.success(salesAssistVO);

    }

    @PutMapping("/{id}")
    @SaCheckPermission("salesAssist:update")
    @Operation(summary = "更新销售辅助记录", description = "根据销售辅助记录ID更新销售辅助信息")
    public Result<SalesAssistVO> salesUpdate(@Parameter(description = "销售辅助记录ID") @PathVariable Long id, @RequestBody SalesAssistUpdateDTO salesAssistUpdateDTO){


        return Result.success(salesAssistService.salesUpdateById(id,salesAssistUpdateDTO));
    }
    @DeleteMapping("/{id}")
    @SaCheckPermission("salesAssist:delete")
    @Operation(summary = "删除销售辅助记录", description = "根据删除辅助记录ID更新销售辅助信息")
    public Result salesDelete(@PathVariable Long id){
        return  salesAssistService.salesDelete(id);
    }



}