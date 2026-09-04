package com.ojbk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ojbk.common.PageResult;
import com.ojbk.common.Result;
import com.ojbk.dto.StoreDTO;
import com.ojbk.dto.PageDTO;
import com.ojbk.dto.StoreUpdateDTO;
import com.ojbk.entity.Store;
import com.ojbk.vo.StoreExportVO;
import com.ojbk.vo.StorePlusVO;
import com.ojbk.vo.StoreVO;

import java.util.List;

public interface StoreService extends IService<Store> {
    public PageResult<StoreVO> storePageList(PageDTO storeDTO);


    public Result<StoreVO> storeCreate(StoreDTO storeCreateDTO);

    public StorePlusVO selectStoreById(Long id);

    public StoreVO  updateStore(Long storeId,StoreUpdateDTO storeUpdateDTO);

    public Result delectStore(Long id);

    List<StoreExportVO> exportStore();





}
