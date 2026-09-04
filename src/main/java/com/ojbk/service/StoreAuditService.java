package com.ojbk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ojbk.common.PageResult;
import com.ojbk.common.Result;
import com.ojbk.dto.PageDTO;
import com.ojbk.dto.StoreAuditDTO;
import com.ojbk.dto.StoreAuditUpdateDTO;
import com.ojbk.entity.StoreAudit;
import com.ojbk.vo.StoreAuditVO;

public interface StoreAuditService extends IService<StoreAudit> {

    public PageResult<StoreAuditVO> storeAuditPage(PageDTO dto);

    public StoreAuditVO storeAuditCreate(StoreAuditDTO dto);

    public StoreAuditVO storeAuditById(Long id);

    public StoreAuditVO storeAuditUpdate(Long id, StoreAuditUpdateDTO dto);

    public Result storeAuditDelect(Long id);

}
