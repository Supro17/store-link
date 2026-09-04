package com.ojbk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ojbk.common.PageResult;
import com.ojbk.common.Result;
import com.ojbk.dto.PageDTO;
import com.ojbk.dto.StoreVerifyDTO;
import com.ojbk.dto.StoreVerifyUpdateDTO;
import com.ojbk.entity.StoreVerify;
import com.ojbk.vo.StoreVerifyVO;

public interface StoreVerifyService extends IService<StoreVerify> {


    public PageResult<StoreVerifyVO> storeVerifyPage(PageDTO pageDTO);

    public StoreVerifyVO storeVerifyCreate(StoreVerifyDTO dto);

    public StoreVerifyVO selectStoreVerifyById(Long id);


    public StoreVerifyVO storeVerifyUpdate(Long id,StoreVerifyUpdateDTO updateDTO);

    public Result storeVerifyDelete(Long id);





}
