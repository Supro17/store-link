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
import com.ojbk.dto.StoreAuditDTO;
import com.ojbk.dto.StoreAuditUpdateDTO;
import com.ojbk.entity.StoreAudit;
import com.ojbk.mapper.StoreAuditMapper;
import com.ojbk.service.StoreAuditService;

import com.ojbk.vo.StoreAuditVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class StoreAuditServiceImpl extends ServiceImpl<StoreAuditMapper, StoreAudit> implements StoreAuditService {

    @Resource
    private StoreAuditMapper storeAuditMapper;

    @Override
    public PageResult<StoreAuditVO> storeAuditPage(PageDTO dto) {

        IPage<StoreAudit> page = new Page<>(dto.getPage(),dto.getSize());

        LambdaQueryWrapper<StoreAudit> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StringUtils.hasText(dto.getKeyword()),StoreAudit::getIssue,dto.getKeyword());

        Object userCode = StpUtil.getSession().get("roleCode");
        Object storeId = StpUtil.getSession().get("storeId");
        if (!"ADMIN".equals(userCode) && !"HQ".equals(userCode) ){
            if (!Objects.isNull(storeId)){
                wrapper.eq(StoreAudit::getStoreId,storeId);
            }
        }

        IPage<StoreAudit> page1 = storeAuditMapper.selectPage(page, wrapper);

        List<StoreAuditVO> collect = page1.getRecords().stream().map(e -> {

            StoreAuditVO vo = new StoreAuditVO();
            BeanUtils.copyProperties(e, vo);
            return vo;

        }).collect(Collectors.toList());

        PageResult<StoreAuditVO> pageResult = new PageResult<>();
        pageResult.setList(collect);
        pageResult.setPage(page1.getPages());
        pageResult.setSize(page1.getSize());
        pageResult.setTotal(page1.getTotal());

        return pageResult;

    }

    @Override
    public StoreAuditVO storeAuditCreate(StoreAuditDTO dto) {

        Long storeId = dto.getStoreId();

        chekPermission(storeId);

        StoreAudit byId = storeAuditMapper.selectById(dto.getSaId());
        if (!Objects.isNull(byId)){

            throw new BusinessException("该督导已存在");
        }

        StoreAudit storeAudit = new StoreAudit();

        storeAudit.setStoreId(dto.getStoreId());
        storeAudit.setScore(dto.getScore());
        storeAudit.setIssue(dto.getIssue());
        storeAudit.setSaId(dto.getSaId());

        Object userId = StpUtil.getSession().get("userId");
        if (userId != null){
            storeAudit.setUserId(Long.valueOf(userId.toString()));
        }


        save(storeAudit);
        StoreAuditVO vo = new StoreAuditVO();

        BeanUtils.copyProperties(storeAudit,vo);
        return vo;
    }

    @Override
    public StoreAuditVO storeAuditById(Long id) {


        StoreAudit storeAudit = storeAuditMapper.selectById(id);



        if (Objects.isNull(storeAudit)){
            throw new BusinessException("该记录不存在");
        }

        chekPermission(storeAudit.getStoreId());


        StoreAuditVO vo = new StoreAuditVO();

        BeanUtils.copyProperties(storeAudit,vo);
        return vo;
    }

    @Override
    public StoreAuditVO storeAuditUpdate(Long id, StoreAuditUpdateDTO dto) {


        StoreAudit storeAudit = storeAuditMapper.selectById(id);

        chekPermission(storeAudit.getStoreId());

        StoreAudit byId = storeAuditMapper.selectById(id);
        if (Objects.isNull(byId)){
            throw new BusinessException("该督导员不存在");
        }


        byId.setStoreId(dto.getStoreId());
        byId.setIssue(dto.getIssue());
        byId.setScore(dto.getScore());
        byId.setSaId(id);


        storeAuditMapper.updateById(byId);

        StoreAuditVO vo = new StoreAuditVO();
        BeanUtils.copyProperties(byId,vo);
        return vo;
    }

    @Override
    public Result storeAuditDelect(Long id) {

        StoreAudit storeAudit = storeAuditMapper.selectById(id);
        chekPermission(storeAudit.getStoreId());


        removeById(id);
        return Result.success("删除成功");
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
