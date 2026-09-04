package com.ojbk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ojbk.common.PageResult;
import com.ojbk.common.Result;
import com.ojbk.dto.PageDTO;
import com.ojbk.dto.StoreVerifyDTO;
import com.ojbk.dto.StoreVerifyUpdateDTO;
import com.ojbk.service.StoreVerifyService;
import com.ojbk.vo.StoreVerifyVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/storeverify")
@Tag(name = "门店核验管理")
public class StoreVerifyController {

    @Resource
    private StoreVerifyService storeVerifyService;

    @GetMapping("/list")
    @SaCheckPermission("verify:list")
    @Operation(summary = "核验列表", description = "分页查询门店核验记录列表")
    public Result<PageResult<StoreVerifyVO>> storeVerifyPage(PageDTO pageDTO){

        return Result.success(storeVerifyService.storeVerifyPage(pageDTO));


    }

    @PostMapping("/create")
    @SaCheckPermission("verify:create")
    @Operation(summary = "创建核验记录", description = "创建门店核验记录，需提供门店ID、编码和核验结果")
    public Result<StoreVerifyVO> storeVerifyCreate(@Valid @RequestBody StoreVerifyDTO dto){


        return Result.success(storeVerifyService.storeVerifyCreate(dto));
    }

    @GetMapping("/{id}")
    @SaCheckPermission("verify:selectById")
    @Operation(summary = "根据ID查询核验记录", description = "根据核验记录ID查询核验详细信息")
    public Result<StoreVerifyVO> selectStoreVerifyById(@Parameter(description = "核验记录ID") @PathVariable Long id){
        return Result.success(storeVerifyService.selectStoreVerifyById(id));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("verify:update")
    @Operation(summary = "更新核验记录", description = "根据核验记录ID更新核验信息")
    public Result<StoreVerifyVO> storeVerifyUpdate(@Parameter(description = "核验记录ID") @PathVariable Long id
            , @Valid @RequestBody StoreVerifyUpdateDTO dto){



       return Result.success( storeVerifyService.storeVerifyUpdate(id,dto));

    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("verify:delete")
    @Operation(summary = "删除核验记录", description = "根据核验记录ID删除核验信息")
    public Result storeVerifyDelect(@PathVariable Long id){
        storeVerifyService.storeVerifyDelete(id);
        return Result.success("删除成功");
    }



}