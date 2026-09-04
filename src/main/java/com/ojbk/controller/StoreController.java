package com.ojbk.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.alibaba.excel.EasyExcel;
import com.ojbk.common.PageResult;
import com.ojbk.common.Result;
import com.ojbk.dto.StoreDTO;
import com.ojbk.dto.PageDTO;
import com.ojbk.dto.StoreUpdateDTO;
import com.ojbk.service.StoreService;
import com.ojbk.vo.StoreExportVO;
import com.ojbk.vo.StorePlusVO;
import com.ojbk.vo.StoreVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/api/v1/store")
@Tag(name = "门店管理")
public class StoreController {


    @Resource
    private StoreService storeService;


    @GetMapping("/export")
    @SaCheckPermission("store:export")
    @Operation(summary = "导出门店",description = "导出门店数据为Excel文件")
    public void exportStore(HttpServletResponse response) throws IOException {


        List<StoreExportVO> storeExportVOS = storeService.exportStore();

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName =  URLEncoder.encode("门店数据", "UTF-8").replaceAll("\\+", "%20");

        response.setHeader("Content-Disposition","attachment;filename*=utf-8''"+fileName+".xlsx");

        EasyExcel.write(response.getOutputStream(), StoreExportVO.class)
                .sheet("门店数据").doWrite(storeExportVOS);

    }






    @GetMapping("/list")
    @SaCheckPermission("store:list")
    @Operation(summary = "门店列表", description = "分页查询门店列表，支持关键字搜索和状态筛选")
    public Result<PageResult<StoreVO>> storeList(@Valid PageDTO storeDTO){

        PageResult<StoreVO> storeVOPageResult = storeService.storePageList(storeDTO);

        return Result.success(storeVOPageResult);

    }

    @PostMapping("/create")
    @SaCheckPermission("store:create")
    @Operation(summary = "创建门店", description = "创建新门店，需提供门店编号、名称、城市和状态")
    public Result<StoreVO> storeCreate(@Valid @RequestBody StoreDTO storeCreateDTO){

       return storeService.storeCreate(storeCreateDTO);

    }


    @GetMapping("/{id}")
    @SaCheckPermission("store:selectById")
    @Operation(summary = "根据ID查询门店", description = "根据门店ID查询门店详细信息")
    public Result<StorePlusVO> selectStoreById(@Parameter(description = "门店ID") @PathVariable Long id){

       return Result.success(storeService.selectStoreById(id));
    }

    @PutMapping("/{storeId}")
    @SaCheckPermission("store:update")
    @Operation(summary = "更新门店", description = "根据门店ID更新门店信息，包括名称、城市和状态")
    public Result<StoreVO> updateStore(@Parameter(description = "门店ID") @PathVariable Long storeId, @Valid @RequestBody StoreUpdateDTO storeUpdateDTO){

        return  Result.success(storeService.updateStore(storeId,storeUpdateDTO));

    }

    @DeleteMapping("/{id}")
    @SaCheckPermission("store:delete")
    @Operation(summary = "删除门店",description = "根据门店Id删除")
    public Result deleteStore(@PathVariable Long id){

        return storeService.delectStore(id);


    }

}