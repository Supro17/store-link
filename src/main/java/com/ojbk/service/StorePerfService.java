package com.ojbk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ojbk.common.PageResult;
import com.ojbk.common.Result;
import com.ojbk.dto.PageDTO;
import com.ojbk.dto.StorePerfDTO;
import com.ojbk.dto.StorePerfUpdateDTO;
import com.ojbk.entity.StorePerf;
import com.ojbk.vo.StorePerfVO;

public interface StorePerfService extends IService<StorePerf> {
    public PageResult<StorePerfVO> storePerfPage(PageDTO dto);


    public StorePerfVO storePerfCreate(StorePerfDTO dto);

    public StorePerfVO storePerfById(Long id);

    public StorePerfVO storePerfUpdate(Long id, StorePerfUpdateDTO dto);
    public Result storePerfDelect(Long id);


}
