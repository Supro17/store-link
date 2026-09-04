package com.ojbk.service.Impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ojbk.common.PageResult;
import com.ojbk.common.Result;
import com.ojbk.common.exception.BusinessException;
import com.ojbk.dto.PageDTO;
import com.ojbk.dto.SalesAssistDTO;
import com.ojbk.dto.SalesAssistUpdateDTO;
import com.ojbk.entity.SalesAssist;
import com.ojbk.mapper.SalesAssistMapper;
import com.ojbk.service.SalesAssistService;
import com.ojbk.vo.SalesAssistVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SalesAssistServiceImpl extends ServiceImpl<SalesAssistMapper, SalesAssist> implements SalesAssistService {


    @Resource
    SalesAssistMapper salesAssistMapper;

    @Override
    public PageResult<SalesAssistVO> salesAssistlistPage(PageDTO pageDTO) {

        IPage<SalesAssist> page = new Page<>(pageDTO.getPage(),pageDTO.getSize());

        LambdaQueryWrapper<SalesAssist> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StringUtils.hasText(pageDTO.getKeyword()),SalesAssist::getName,pageDTO.getKeyword());


        SaSession session = StpUtil.getSession();
        Object roleCode = session.get("roleCode");
        Object storeId = session.get("storeId");

        if (! "ADMIN".equals(roleCode) && !"HQ".equals(roleCode)){
            if (!Objects.isNull(storeId)){
                wrapper.eq(SalesAssist::getStoreId,storeId);
            }
        }


        IPage<SalesAssist> salesAssistIPage = salesAssistMapper.selectPage(page, wrapper);

        List<SalesAssistVO> salesAssistVOS = salesAssistIPage.getRecords().stream().map(entity -> {
            SalesAssistVO salesAssistVO = new SalesAssistVO();
            BeanUtils.copyProperties(entity, salesAssistVO);
            return salesAssistVO;
        }).toList();


        PageResult<SalesAssistVO> salesAssistVOPageResult = new PageResult<>();
        salesAssistVOPageResult.setList(salesAssistVOS);
        salesAssistVOPageResult.setSize(salesAssistIPage.getSize());
        salesAssistVOPageResult.setPage(salesAssistIPage.getPages());
        salesAssistVOPageResult.setTotal(salesAssistIPage.getTotal());


        return salesAssistVOPageResult;


    }

    @Override
    public SalesAssistVO salesAssistCreate(SalesAssistDTO salesAssistDTO) {

        chekPermission(salesAssistDTO.getStoreId());

        SalesAssist salesAssist = salesAssistMapper.selectById(salesAssistDTO.getSaId());

        if (!Objects.isNull(salesAssist)){
             throw new BusinessException("该导购已存在");

        }

        SalesAssist assist = new SalesAssist();

        assist.setSales(salesAssistDTO.getSales());
        assist.setName(salesAssistDTO.getName());
        assist.setStoreId(salesAssistDTO.getStoreId());
        assist.setSaId(salesAssistDTO.getSaId());

        save(assist);

        SalesAssistVO vo = new SalesAssistVO();

        BeanUtils.copyProperties(assist,vo);

        return vo;




    }

    @Override
    public SalesAssistVO selectsalesAssistById(Long id) {
//        chekPermission();

        SalesAssist salesAssist1 = salesAssistMapper.selectById(id);

        if (!Objects.nonNull(salesAssist1)){

            throw new BusinessException("该导购不存在");
        }



        chekPermission(salesAssist1.getStoreId());

        SalesAssistVO salesAssistVO = new SalesAssistVO();
        BeanUtils.copyProperties(salesAssist1,salesAssistVO);


       return salesAssistVO;


    }

    @Override
    public SalesAssistVO salesUpdateById(Long id, SalesAssistUpdateDTO dto) {


        SalesAssist salesAssist1 = salesAssistMapper.selectById(id);
        Long storeId = salesAssist1.getStoreId();

        if (Objects.isNull(storeId)){
            throw  new BusinessException("该记录不存在");
        }

        chekPermission(storeId);



        SalesAssist salesAssist = salesAssistMapper.selectById(id);
        if (Objects.isNull(salesAssist)){
            throw new BusinessException("该导购不存在");
        }


        salesAssist.setName(dto.getName());
        salesAssist.setSales(dto.getSales());
        salesAssist.setStoreId(dto.getStoreId());

        salesAssistMapper.updateById(salesAssist);

        SalesAssistVO salesAssistVO = new SalesAssistVO();

        BeanUtils.copyProperties(salesAssist,salesAssistVO);

        return salesAssistVO;
    }

    @Override
    public Result salesDelete(Long id) {

        SalesAssist salesAssist = salesAssistMapper.selectById(id);

        chekPermission(salesAssist.getStoreId());

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
