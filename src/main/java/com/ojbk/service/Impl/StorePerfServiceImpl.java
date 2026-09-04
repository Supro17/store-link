package com.ojbk.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ojbk.common.PageResult;
import com.ojbk.common.Result;
import com.ojbk.common.exception.BusinessException;
import com.ojbk.dto.PageDTO;
import com.ojbk.dto.StorePerfDTO;
import com.ojbk.dto.StorePerfUpdateDTO;
import com.ojbk.entity.Store;
import com.ojbk.entity.StorePerf;
import com.ojbk.mapper.StoreMapper;
import com.ojbk.mapper.StorePerfMapper;
import com.ojbk.service.StorePerfService;
import com.ojbk.vo.StorePerfVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Service
public class StorePerfServiceImpl extends ServiceImpl<StorePerfMapper, StorePerf> implements StorePerfService {

    @Resource
    private StorePerfMapper storePerfMapper;

    @Resource
    private StoreMapper storeMapper;

    @Override
    public PageResult<StorePerfVO> storePerfPage(PageDTO dto) {

        IPage<StorePerf> page = new Page<>(dto.getPage(),dto.getSize());

        LambdaQueryWrapper<StorePerf> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(dto.getKeyword()),StorePerf::getSpId,dto.getKeyword());

        Object userCode = StpUtil.getSession().get("roleCode");
        Object storeId = StpUtil.getSession().get("storeId");
        if (!"ADMIN".equals(userCode) && !"HQ".equals(userCode)){
            if (!Objects.isNull(storeId)){
                wrapper.eq(StorePerf::getStoreId,storeId);
            }
        }


        IPage<StorePerf> perfIPage = storePerfMapper.selectPage(page, wrapper);

        List<StorePerfVO> collect = perfIPage.getRecords().stream().map(e -> {
            StorePerfVO vo = new StorePerfVO();
            BeanUtils.copyProperties(e, vo);
            fillStoreName(vo);
            return vo;
        }).collect(Collectors.toList());

        PageResult<StorePerfVO> voPageResult = new PageResult<>();
        voPageResult.setList(collect);
        voPageResult.setPage(perfIPage.getPages());
        voPageResult.setSize(perfIPage.getSize());
        voPageResult.setTotal(perfIPage.getTotal());


        return voPageResult;
    }

    @Override
    public StorePerfVO storePerfCreate(StorePerfDTO dto) {

        Long storeId = dto.getStoreId();
        chekPermission(storeId);

        StorePerf storePerf = storePerfMapper.selectById(dto.getSpId());

        if (!Objects.isNull(storePerf)){
            throw new BusinessException("该门店业绩已存在");
        }

        StorePerf perf = new StorePerf();

        perf.setSpId(dto.getSpId());
        perf.setStoreId(dto.getStoreId());
        perf.setRevenue(dto.getRevenue());
        perf.setTarget(dto.getTarget());
        save(perf);

        StorePerfVO vo = new StorePerfVO();
        BeanUtils.copyProperties(perf,vo);

        fillStoreName(vo);

        return vo;
    }

    @Override
    public StorePerfVO storePerfById(Long id) {

        StorePerf storePerf = storePerfMapper.selectById(id);


        if (Objects.isNull(storePerf)){

            throw new BusinessException("该记录不存在");
        }

        chekPermission(storePerf.getStoreId());


        StorePerfVO vo = new StorePerfVO();

        BeanUtils.copyProperties(storePerf,vo);
        fillStoreName(vo);

        return vo;
    }

    @Override
    public StorePerfVO storePerfUpdate(Long id, StorePerfUpdateDTO dto) {


        Long storeId = storePerfMapper.selectById(id).getStoreId();
        chekPermission(storeId);

        StorePerf byId = storePerfMapper.selectById(id);
        if (Objects.isNull(byId)){
            throw new BusinessException("该门店业绩数据不存在");
        }

        byId.setStoreId(dto.getStoreId());
        byId.setRevenue(dto.getRevenue());
        byId.setTarget(dto.getTarget());





        storePerfMapper.updateById(byId);
        StorePerfVO vo = new StorePerfVO();
        BeanUtils.copyProperties(byId,vo);

        fillStoreName(vo);


        return vo;
    }

    @Override
    public Result storePerfDelect(Long id) {


        StorePerf storePerf = storePerfMapper.selectById(id);

        chekPermission(storePerf.getStoreId());

        removeById(id);

        return Result.success("删除成功",null);
    }


    private void chekPermission(Long targetStoreId){
        Object roleCode = StpUtil.getSession().get("roleCode");

        if ("ADMIN".equals(roleCode) || "HQ".equals(roleCode)){

            return;
        }
        Object storeId = StpUtil.getSession().get("storeId");

        if (storeId == null || ! storeId.toString().equals(String.valueOf(targetStoreId))){
            throw new BusinessException("权限不足，只能操作自己门店的数据");

        }
    }


    private void fillStoreName(StorePerfVO vo){

        if (vo != null && vo.getStoreId() !=null){
            Store store = storeMapper.selectById(vo.getStoreId());

            if (store != null){
                vo.setStoreName(store.getName());
            }
        }

    }
}
