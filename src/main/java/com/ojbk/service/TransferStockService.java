package com.ojbk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ojbk.common.PageResult;
import com.ojbk.common.Result;
import com.ojbk.dto.PageDTO;
import com.ojbk.dto.TransferStockDTO;
import com.ojbk.dto.TransferStockUpdateDTO;
import com.ojbk.entity.TransferStock;
import com.ojbk.vo.TransferStockVO;

public interface TransferStockService extends IService<TransferStock> {


    public PageResult<TransferStockVO> transferStockPage(PageDTO dto);


    public TransferStockVO transferStockCreate(TransferStockDTO dto);


    public TransferStockVO transferStockById(Long id);


    public TransferStockVO transferStockUpdate(Long id,TransferStockUpdateDTO dto);

    public Result deleteTransferStock(Long id);
}
