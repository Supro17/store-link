package com.ojbk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ojbk.common.PageResult;
import com.ojbk.common.Result;
import com.ojbk.dto.PageDTO;
import com.ojbk.dto.StorePerfDTO;
import com.ojbk.dto.StorePerfUpdateDTO;
import com.ojbk.service.StorePerfService;
import com.ojbk.vo.StorePerfVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/storeperf")
@Tag(name = "门店绩效管理")
public class StorePerfController {

    @Resource
    StorePerfService storePerfService;

    @GetMapping("/list")
    @SaCheckPermission("perf:list")
    @Operation(summary = "绩效列表", description = "分页查询门店绩效记录列表")
    public Result<PageResult<StorePerfVO>> storePerfPage(PageDTO dto){
        return Result.success(storePerfService.storePerfPage(dto));

    }

    @PostMapping("/create")
    @SaCheckPermission("perf:create")
    @Operation(summary = "创建绩效记录", description = "创建门店绩效记录，需提供门店ID、营收和目标值")
    public Result<StorePerfVO> storePerfCreate(@RequestBody StorePerfDTO dto){
        return Result.success(storePerfService.storePerfCreate(dto));
    }

    @GetMapping("/{id}")
    @SaCheckPermission("perf:selectById")
    @Operation(summary = "根据ID查询绩效记录", description = "根据绩效记录ID查询绩效详细信息")
    public Result<StorePerfVO> storePerfById(@Parameter(description = "绩效记录ID") @PathVariable Long id){

        return Result.success(storePerfService.storePerfById(id));
    }

    @PutMapping("/{id}")
    @SaCheckPermission("perf:update")
    @Operation(summary = "更新绩效记录", description = "根据绩效记录ID更新绩效信息")
    public Result<StorePerfVO> storePerfUpdate(@Parameter(description = "绩效记录ID") @PathVariable Long id, @RequestBody StorePerfUpdateDTO dto){
        return Result.success(storePerfService.storePerfUpdate(id,dto));

    }
    @DeleteMapping("/{id}")
    @SaCheckPermission("perf:delete")
    @Operation(summary = "删除绩效记录", description = "根据删除记录ID更新绩效信息")
    public Result storePerfDelect(@PathVariable Long id){

        return storePerfService.storePerfDelect(id);
    }


}