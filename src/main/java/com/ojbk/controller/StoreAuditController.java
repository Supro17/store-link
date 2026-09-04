package com.ojbk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ojbk.common.PageResult;
import com.ojbk.common.Result;
import com.ojbk.dto.PageDTO;
import com.ojbk.dto.StoreAuditDTO;
import com.ojbk.dto.StoreAuditUpdateDTO;
import com.ojbk.service.StoreAuditService;
import com.ojbk.vo.StoreAuditVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/storeaudit")
@Tag(name = "门店审核管理")
public class StoreAuditController {

    @Resource
    private StoreAuditService storeAuditService;

    @GetMapping("/list")
    @SaCheckPermission("audit:list")
    @Operation(summary = "审核列表", description = "分页查询门店审核记录列表")
    public Result<PageResult<StoreAuditVO>> storeAuditPage(@Valid PageDTO dto){
        return Result.success(storeAuditService.storeAuditPage(dto));
    }

    @PostMapping("/create")
    @SaCheckPermission("audit:create")
    @Operation(summary = "创建审核记录", description = "创建门店审核记录，需提供门店ID、问题描述和评分")
    public Result<StoreAuditVO> storeAuditCreate(@RequestBody StoreAuditDTO dto){
        return Result.success(storeAuditService.storeAuditCreate(dto));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("audit:update")
    @Operation(summary = "更新审核记录", description = "根据审核ID更新审核记录信息")
    public Result<StoreAuditVO> storeAuditUpdate(@Parameter(description = "审核记录ID") @PathVariable Long id, @RequestBody StoreAuditUpdateDTO dto){
        return Result.success(storeAuditService.storeAuditUpdate(id,dto));
    }

    @GetMapping("/{id}")
    @SaCheckPermission("audit:selectById")
    @Operation(summary = "根据ID查询审核记录", description = "根据审核记录ID查询审核详细信息")
    public Result<StoreAuditVO> storeAuditById(@Parameter(description = "审核记录ID") @PathVariable Long id){
        return  Result.success(storeAuditService.storeAuditById(id));
    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("audit:delete")
    @Operation(summary = "根据ID删除审核记录", description = "根据审核记录ID删除审核详细信息")
    public Result storeAuditDelect(@PathVariable Long id){
        return storeAuditService.storeAuditDelect(id);
    }


}