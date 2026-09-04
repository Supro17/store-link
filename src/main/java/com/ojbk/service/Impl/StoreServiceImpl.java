package com.ojbk.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ojbk.common.PageResult;
import com.ojbk.common.Result;
import com.ojbk.common.exception.BusinessException;
import com.ojbk.dto.StoreDTO;
import com.ojbk.dto.PageDTO;
import com.ojbk.dto.StoreUpdateDTO;
import com.ojbk.entity.SalesAssist;
import com.ojbk.entity.Store;
import com.ojbk.entity.StoreAudit;
import com.ojbk.entity.StorePerf;
import com.ojbk.mapper.SalesAssistMapper;
import com.ojbk.mapper.StoreAuditMapper;
import com.ojbk.mapper.StoreMapper;
import com.ojbk.mapper.StorePerfMapper;
import com.ojbk.service.StoreService;
import com.ojbk.vo.*;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements StoreService {

    @Resource
    private StoreMapper storeMapper;

    @Resource
    StorePerfMapper storePerfMapper;

    @Resource
    StoreAuditMapper storeAuditMapper;

    @Resource
    SalesAssistMapper salesAssistMapper;


    @Override
    public PageResult<StoreVO> storePageList(PageDTO storeDTO){


        Object roleCode = StpUtil.getSession().get("roleCode");
        Object userStoreId = StpUtil.getSession().get("storeId");


        IPage<Store> page = new Page<>(storeDTO.getPage(),storeDTO.getSize());

        LambdaQueryWrapper<Store> wrapper  = new LambdaQueryWrapper<>();

        wrapper.like(StringUtils.hasText(storeDTO.getKeyword()),Store::getName,storeDTO.getKeyword())
                .eq(StringUtils.hasText(storeDTO.getStatus()),Store::getStatus,storeDTO.getStatus());


        if (!"ADMIN".equals(roleCode) && !"HQ".equals(roleCode)) {
            if (userStoreId != null) {
                wrapper.eq(Store::getStoreId, userStoreId);
            }
        }

        IPage<Store> storeIPage = storeMapper.selectPage(page, wrapper);

        List<StoreVO> storeVOS = storeIPage.getRecords().stream().map(entity -> {
            StoreVO storeVO = new StoreVO();
            BeanUtils.copyProperties(entity, storeVO);
            return storeVO;
        }).collect(Collectors.toList());


        PageResult<StoreVO> storeVOPageResult = new PageResult<>();

        storeVOPageResult.setList(storeVOS);
        storeVOPageResult.setTotal(storeIPage.getTotal());
        storeVOPageResult.setPage(storeIPage.getPages());
        storeVOPageResult.setSize(storeIPage.getSize());

        return storeVOPageResult;



    }

    @Override
    public Result<StoreVO> storeCreate(StoreDTO storeCreateDTO) {

        chekPermission(storeCreateDTO.getStoreId());

        Store byId = getById(storeCreateDTO.getStoreId());

        if (Objects.isNull(byId)){
            Store store = new Store();
            store.setName(storeCreateDTO.getName());
            store.setCity(storeCreateDTO.getCity());
            store.setStoreId(storeCreateDTO.getStoreId());
            store.setStatus(storeCreateDTO.getStatus());
            save(store);

            StoreVO vo =new StoreVO();
            BeanUtils.copyProperties(store,vo);
            return Result.success(vo);
        }

        return Result.fail("该店已存在");
    }

    @Override
    public StorePlusVO selectStoreById(Long id) {

        if (Objects.isNull(id)){
            throw new BusinessException("该门店不存在");
        }

        chekPermission(id);


        StorePlusVO plusVO = new StorePlusVO();

        Store store = storeMapper.selectById(id);
        if (Objects.isNull(store)){
            throw new BusinessException("该门店不存在");
        }
        plusVO.setStoreId(store.getStoreId());
        plusVO.setStatus(store.getStatus());
        plusVO.setName(store.getName());
        plusVO.setCity(store.getCity());

        LambdaQueryWrapper<SalesAssist> wrapper1 = new LambdaQueryWrapper<>();

        wrapper1.eq(SalesAssist::getStoreId,id);
        List<SalesAssist> assistList = salesAssistMapper.selectList(wrapper1);


        List<SalesAssistVO> collect1 = assistList.stream().map(e -> {
            SalesAssistVO salesAssistVO = new SalesAssistVO();
            BeanUtils.copyProperties(e, salesAssistVO);
            return salesAssistVO;
        }).collect(Collectors.toList());


        LambdaQueryWrapper<StoreAudit> wrapper2 = new LambdaQueryWrapper<>();

        wrapper2.eq(StoreAudit::getStoreId,id);
        List<StoreAudit> storeAuditList = storeAuditMapper.selectList(wrapper2);

        List<StoreAuditVO> collect2 = storeAuditList.stream().map(e -> {
            StoreAuditVO storeAuditVO = new StoreAuditVO();

            BeanUtils.copyProperties(e, storeAuditVO);
            return storeAuditVO;
        }).collect(Collectors.toList());


        LambdaQueryWrapper<StorePerf> wrapper3 = new LambdaQueryWrapper<>();

        wrapper3.eq(StorePerf::getStoreId,id);

        List<StorePerf> storePerfList = storePerfMapper.selectList(wrapper3);

        List<StorePerfVO> collect3 = storePerfList.stream().map(e -> {
            StorePerfVO storePerfVO = new StorePerfVO();

            BeanUtils.copyProperties(e, storePerfVO);
            return storePerfVO;
        }).collect(Collectors.toList());

        plusVO.setSalesAssistVOList(collect1);
        plusVO.setStoreAuditVOList(collect2);
        plusVO.setStorePerfVOList(collect3);

        return plusVO;
    }

    @Override
    @Transactional
    public StoreVO updateStore(Long storeId,StoreUpdateDTO storeUpdateDTO) {

        chekPermission(storeId);

        Store store = storeMapper.selectById(storeId);

        if (Objects.isNull(store)){
            throw new BusinessException("该店不存在");
        }

        store.setStatus(storeUpdateDTO.getStatus());
        store.setCity(storeUpdateDTO.getCity());
        store.setName(storeUpdateDTO.getName());



        storeMapper.updateById(store);
        StoreVO storeVO = new StoreVO();

        BeanUtils.copyProperties(store,storeVO);
        return storeVO;

    }

    @Override
    public Result delectStore(Long id) {

        chekPermission(id);

        removeById(id);
        return Result.success("删除成功",null);

    }

    @Override
    public List<StoreExportVO> exportStore() {

        Object roleCode = StpUtil.getSession().get("roleCode");
        Object storeId = StpUtil.getSession().get("storeId");
        LambdaQueryWrapper<Store> wrapper = new LambdaQueryWrapper<>();

        if (!"ADMIN".equals(roleCode) && !"HQ".equals(roleCode)){
            if (!Objects.isNull(storeId)){
                wrapper.eq(Store::getStoreId,storeId);

            }
        }
        List<Store> storeList = storeMapper.selectList(wrapper);

        List<StoreExportVO> collect = storeList.stream().map(e -> {

            StoreExportVO storeExportVO = new StoreExportVO();

            storeExportVO.setStoreId(e.getStoreId());
            storeExportVO.setName(e.getName());
            storeExportVO.setCity(e.getCity());
            storeExportVO.setStatus(e.getStatus());
            if (e.getCreatedAt() != null){
                storeExportVO.setCreatedAt(e.getCreatedAt()
                        .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }

            return storeExportVO;
        }).collect(Collectors.toList());

        return collect;
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





}