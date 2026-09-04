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
import com.ojbk.dto.StoreVerifyDTO;
import com.ojbk.dto.StoreVerifyUpdateDTO;
import com.ojbk.entity.StoreVerify;
import com.ojbk.mapper.StoreVerifyMapper;
import com.ojbk.service.StoreVerifyService;
import com.ojbk.vo.StoreVerifyVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StoreVerifyServiceImpl extends ServiceImpl<StoreVerifyMapper, StoreVerify> implements StoreVerifyService {


    @Resource
    private StoreVerifyMapper storeVerifyMapper;


    @Override
    public PageResult<StoreVerifyVO> storeVerifyPage(PageDTO pageDTO) {

        IPage<StoreVerify> page =new Page<>(pageDTO.getPage(),pageDTO.getSize());


        LambdaQueryWrapper<StoreVerify> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(pageDTO.getKeyword()),StoreVerify::getCode,pageDTO.getKeyword());

        Object userCode = StpUtil.getSession().get("roleCode");
        Object storeId = StpUtil.getSession().get("storeId");

        if (!"ADMIN".equals(userCode) && !"HQ".equals(userCode)){
            if (!Objects.isNull(storeId)){
                wrapper.eq(StoreVerify::getStoreId,storeId);
            }
        }


        IPage<StoreVerify> verifyIPage = storeVerifyMapper.selectPage(page, wrapper);

        List<StoreVerifyVO> collect = verifyIPage.getRecords().stream().map(entity -> {

            StoreVerifyVO storeVerifyVO = new StoreVerifyVO();

            BeanUtils.copyProperties(entity, storeVerifyVO);

            return storeVerifyVO;
        }).collect(Collectors.toList());

        PageResult<StoreVerifyVO> storeVerifyVOPageResult = new PageResult<>();

        storeVerifyVOPageResult.setList(collect);
        storeVerifyVOPageResult.setPage(verifyIPage.getPages());
        storeVerifyVOPageResult.setSize(verifyIPage.getSize());
        storeVerifyVOPageResult.setTotal(verifyIPage.getTotal());


        return storeVerifyVOPageResult;
    }

    @Override
    public StoreVerifyVO storeVerifyCreate(StoreVerifyDTO dto) {

        Long storeId = dto.getStoreId();
        chekPermission(storeId);


        StoreVerify storeVerify = storeVerifyMapper.selectById(dto.getSvId());

        if (!Objects.isNull(storeVerify)){
            throw new BusinessException("该报销已存在");
        }
        StoreVerify storeVerify1 = new StoreVerify();

        storeVerify1.setStoreId(dto.getStoreId());
        storeVerify1.setOk(dto.getOk());
        storeVerify1.setCode(dto.getCode());
        storeVerify1.setSvId(dto.getSvId());
        save(storeVerify1);

        StoreVerifyVO vo = new StoreVerifyVO();
        BeanUtils.copyProperties(storeVerify1,vo);

        return vo;
    }

    @Override
    public StoreVerifyVO selectStoreVerifyById(Long id) {

        StoreVerify byId = storeVerifyMapper.selectById(id);

        if (Objects.isNull(byId)){
            throw new BusinessException("该记录不能存在");
        }

        chekPermission(byId.getStoreId());

        StoreVerify storeVerify = storeVerifyMapper.selectById(id);
        if (Objects.isNull(storeVerify)){
            throw new BusinessException("该报销不存在");
        }

        StoreVerifyVO vo = new StoreVerifyVO();

        vo.setCode(storeVerify.getCode());
        vo.setOk(storeVerify.getOk());
        vo.setStoreId(storeVerify.getStoreId());
        vo.setSvId(storeVerify.getSvId());
        return vo;


    }

    @Override
    public StoreVerifyVO storeVerifyUpdate(Long id,StoreVerifyUpdateDTO updateDTO) {

        Long storeId = storeVerifyMapper.selectById(id).getStoreId();

        chekPermission(storeId);

        StoreVerify byId = storeVerifyMapper.selectById(id);
        if (Objects.isNull(byId)){
            throw new BusinessException("该报销不存在");
        }

        byId.setStoreId(updateDTO.getStoreId());
        byId.setOk(updateDTO.getOk());
        byId.setCode(updateDTO.getCode());
        storeVerifyMapper.updateById(byId);


        StoreVerifyVO vo = new StoreVerifyVO();


        BeanUtils.copyProperties(byId,vo);


        return vo;
    }

    @Override
    public Result storeVerifyDelete(Long id) {

        Long storeId = storeVerifyMapper.selectById(id).getStoreId();

        chekPermission(storeId);

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
}

