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
import com.ojbk.dto.TransferStockDTO;
import com.ojbk.dto.TransferStockUpdateDTO;
import com.ojbk.entity.TransferStock;
import com.ojbk.mapper.TransferStockMapper;
import com.ojbk.service.TransferStockService;
import com.ojbk.vo.TransferStockVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TransferStockServiceImpl extends ServiceImpl<TransferStockMapper, TransferStock> implements TransferStockService {


    @Resource
    private TransferStockMapper transferStockMapper;

    @Override
    public PageResult<TransferStockVO> transferStockPage(PageDTO dto) {

        IPage<TransferStock> page = new Page<>(dto.getPage(),dto.getSize());

        LambdaQueryWrapper<TransferStock> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StringUtils.hasText(dto.getKeyword()),TransferStock::getSku,dto.getKeyword());

        Object userCode = StpUtil.getSession().get("roleCode");
        Object storeId = StpUtil.getSession().get("storeId");

        if (!"ADMIN".equals(userCode) && !"HQ".equals(userCode)){
            if (!Objects.isNull(storeId)){
                wrapper.eq(TransferStock::getToStoreId,storeId);
            }
        }

        IPage<TransferStock> page1 = transferStockMapper.selectPage(page, wrapper);

        List<TransferStockVO> collect = page1.getRecords().stream().map(e -> {

            TransferStockVO vo = new TransferStockVO();
            BeanUtils.copyProperties(e, vo);

            return vo;
        }).collect(Collectors.toList());

        PageResult<TransferStockVO> transferStockVOPageResult = new PageResult<>();

        transferStockVOPageResult.setList(collect);
        transferStockVOPageResult.setPage(page1.getPages());
        transferStockVOPageResult.setSize(page1.getSize());
        transferStockVOPageResult.setTotal(page1.getTotal());

        return transferStockVOPageResult;


    }

    @Override
    public TransferStockVO transferStockCreate(TransferStockDTO dto) {

        Long fromStoreId = dto.getFromStoreId();

        chekPermission(fromStoreId);


        TransferStock byId = transferStockMapper.selectById(dto.getTsId());


        if (!Objects.isNull(byId)){
            throw new BusinessException("该调拨已存在");
        }

        TransferStock transferStock = new TransferStock();

        transferStock.setSku(dto.getSku());
        transferStock.setFromStoreId(dto.getFromStoreId());
        transferStock.setToStoreId(dto.getToStoreId());
        transferStock.setTsId(dto.getTsId());

        save(transferStock);

        TransferStockVO vo = new TransferStockVO();

        BeanUtils.copyProperties(transferStock,vo);

        return vo;
    }

    @Override
    public TransferStockVO transferStockById(Long id) {


        TransferStock transferStock1 = transferStockMapper.selectById(id);

        if (Objects.isNull(transferStock1.getFromStoreId())){

            throw new BusinessException("该记录不存在");

        }
        chekPermission(transferStock1.getFromStoreId());


        TransferStock transferStock = transferStockMapper.selectById(id);
        if (Objects.isNull(transferStock)){
            throw new BusinessException("该库存调拨不存在");
        }

        TransferStockVO vo = new TransferStockVO();


        BeanUtils.copyProperties(transferStock,vo);

        return vo;

    }

    @Override
    public TransferStockVO transferStockUpdate(Long id,TransferStockUpdateDTO dto) {


        TransferStock transferStock = transferStockMapper.selectById(id);

        chekPermission(transferStock.getFromStoreId());


        TransferStock byId = transferStockMapper.selectById(id);

        if (Objects.isNull(byId)){
            throw new BusinessException("该库存调拨不存在");
        }

        byId.setSku(dto.getSku());
        byId.setToStoreId(dto.getToStoreId());
        byId.setTsId(id);
        byId.setFromStoreId(dto.getFromStoreId());



        transferStockMapper.updateById(byId);

        TransferStockVO vo = new TransferStockVO();
        BeanUtils.copyProperties(byId,vo);
        return vo;

    }

    @Override
    public Result deleteTransferStock(Long id) {

        TransferStock transferStock = transferStockMapper.selectById(id);
        chekPermission(transferStock.getFromStoreId());

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
