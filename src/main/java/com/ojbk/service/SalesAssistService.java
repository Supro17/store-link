package com.ojbk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ojbk.common.PageResult;
import com.ojbk.common.Result;
import com.ojbk.dto.PageDTO;
import com.ojbk.dto.SalesAssistDTO;
import com.ojbk.dto.SalesAssistUpdateDTO;
import com.ojbk.entity.SalesAssist;
import com.ojbk.vo.SalesAssistVO;

public interface SalesAssistService extends IService<SalesAssist> {


    public PageResult<SalesAssistVO> salesAssistlistPage(PageDTO pageDTO);


    public SalesAssistVO salesAssistCreate(SalesAssistDTO salesAssistDTO);

    public SalesAssistVO selectsalesAssistById(Long id);


    public SalesAssistVO salesUpdateById(Long id, SalesAssistUpdateDTO assistUpdateDTO);

    public Result salesDelete(Long id);
}
