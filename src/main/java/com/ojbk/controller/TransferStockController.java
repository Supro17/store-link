package com.ojbk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ojbk.common.PageResult;
import com.ojbk.common.Result;
import com.ojbk.dto.PageDTO;
import com.ojbk.dto.TransferStockDTO;
import com.ojbk.dto.TransferStockUpdateDTO;
import com.ojbk.service.TransferStockService;
import com.ojbk.vo.TransferStockVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transferstock")
@Tag(name = "调拨库存管理")
public class TransferStockController {

    @Resource
    private TransferStockService transferStockService;

    @GetMapping("/list")
    @SaCheckPermission("transfer:list")
    @Operation(summary = "调拨库存列表", description = "分页查询调拨库存记录列表")
    public Result<PageResult<TransferStockVO>> transferStockPage(PageDTO dto){
        return Result.success(transferStockService.transferStockPage(dto));
    }

    @PostMapping("/create")
    @SaCheckPermission("transfer:create")
    @Operation(summary = "创建调拨记录", description = "创建调拨库存记录，需提供调出门店ID、调入门店ID和SKU")
    public Result<TransferStockVO> transferStockCreate(@RequestBody TransferStockDTO dto){

        return Result.success(transferStockService.transferStockCreate(dto));
    }


    @GetMapping("/{id}")
    @SaCheckPermission("transfer:selectById")
    @Operation(summary = "根据ID查询调拨记录", description = "根据调拨记录ID查询调拨详细信息")
    public Result<TransferStockVO> transferStockById(@Parameter(description = "调拨记录ID") @PathVariable Long id){

       return Result.success( transferStockService.transferStockById(id));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("transfer:update")
    @Operation(summary = "更新调拨记录", description = "根据调拨记录ID更新调拨信息")
    public Result<TransferStockVO> transferStockUpdate(@Parameter(description = "调拨记录ID") @PathVariable Long id, @RequestBody TransferStockUpdateDTO dto){
        return Result.success(transferStockService.transferStockUpdate(id,dto));


    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("transfer:delete")
    @Operation(summary = "删除调拨数据",description = "根据id删除调拨数据")
    public Result transferStockDelect(@PathVariable Long id){
        return transferStockService.deleteTransferStock(id);
    }

}