package com.ojbk.service.Impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ojbk.entity.*;
import com.ojbk.mapper.*;
import com.ojbk.service.DashboardService;
import com.ojbk.vo.DashboardVO;
import com.ojbk.vo.StoreRankingVO;
import com.ojbk.vo.SupervisorAuditItemVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Resource
    private StoreAuditMapper storeAuditMapper;

    @Resource
    private StoreMapper storeMapper;

    @Resource
    private SalesAssistMapper salesAssistMapper;

    @Resource
    private StorePerfMapper storePerfMapper;

    @Resource
    private StoreVerifyMapper storeVerifyMapper;

    @Resource
    private  UserMapper userMapper;

    @Resource
    private TransferStockMapper transferStockMapper;




    @Override
    public DashboardVO getDashboardVO() {


        Object roleCode = StpUtil.getSession().get("roleCode");
        Object storeId = StpUtil.getSession().get("storeId");

      boolean isAdminOrHQ =  "ADMIN".equals(roleCode) || "HQ".equals(roleCode);
      Long currentStoreId = Objects.isNull(storeId)? null :Long.valueOf(storeId.toString());

        DashboardVO dashboardVO = new DashboardVO();

        LambdaQueryWrapper<Store> wrapper1 = new LambdaQueryWrapper<>();

        if (!isAdminOrHQ && currentStoreId != null){
            wrapper1.eq(Store::getStoreId,currentStoreId);
        }

        dashboardVO.setTotalStore(storeMapper.selectCount(wrapper1));

        LambdaQueryWrapper<Store> wrapper2 = new LambdaQueryWrapper<>();

        wrapper2.eq(Store::getStatus,"active");

        if (!isAdminOrHQ && currentStoreId != null){
            wrapper2.eq(Store::getStoreId,currentStoreId);
        }
        dashboardVO.setActiveStore(storeMapper.selectCount(wrapper2));



        LambdaQueryWrapper<StorePerf> wrapper3 = new LambdaQueryWrapper<>();

        if (!isAdminOrHQ && currentStoreId != null){
            wrapper3.eq(StorePerf::getStoreId,currentStoreId);
        }

        List<StorePerf> storePerfList = storePerfMapper.selectList(wrapper3);
        BigDecimal totalRevenue = storePerfList.stream().map(StorePerf::getRevenue)
                        .filter(Objects::nonNull).reduce(BigDecimal.ZERO,BigDecimal::add);


        BigDecimal totalTarget = storePerfList.stream().map(StorePerf::getTarget)
                        .filter(Objects::nonNull).reduce(BigDecimal.ZERO,BigDecimal::add);
        dashboardVO.setTotalRevenue(totalRevenue);
        dashboardVO.setTotalTarget(totalTarget);


        if (totalTarget.compareTo(BigDecimal.ZERO) > 0) {

            dashboardVO.setAchievementRate(totalRevenue.multiply(new BigDecimal("100"))
                    .divide(totalTarget,1,RoundingMode.HALF_UP));
        }

        LambdaQueryWrapper<SalesAssist> wrapper4 = new LambdaQueryWrapper<>();
        if (!isAdminOrHQ && currentStoreId!= null){
            wrapper4.eq(SalesAssist::getStoreId,currentStoreId);
        }
        dashboardVO.setTotalSalesAssist(salesAssistMapper.selectCount(wrapper4));


        LambdaQueryWrapper<StoreVerify> wrapper5 = new LambdaQueryWrapper<>();

        if (!isAdminOrHQ && currentStoreId != null){
            wrapper5.eq(StoreVerify::getStoreId,currentStoreId);
        }

        dashboardVO.setTotalVerify(storeVerifyMapper.selectCount(wrapper5));

        LambdaQueryWrapper<StoreVerify> wrapper6 = new LambdaQueryWrapper<>();

        wrapper6.eq(StoreVerify::getOk,1);
        if (!isAdminOrHQ && currentStoreId != null){
            wrapper6.eq(StoreVerify::getStoreId,currentStoreId);
        }

        dashboardVO.setSuccessVerify(storeVerifyMapper.selectCount(wrapper6));

        LambdaQueryWrapper<StoreAudit> wrapper7 = new LambdaQueryWrapper<>();

        if (!isAdminOrHQ &&currentStoreId != null){
            wrapper7.eq(StoreAudit::getStoreId,currentStoreId);
        }
        List<StoreAudit> storeAuditList = storeAuditMapper.selectList(wrapper7);
        dashboardVO.setTotalAudit((long) storeAuditList.size());

        if (!storeAuditList.isEmpty()){
            BigDecimal avg = storeAuditList.stream().map(StoreAudit::getScore)
                    .filter(Objects::nonNull).map(score -> new BigDecimal(score))
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(new BigDecimal(storeAuditList.size()), 1, RoundingMode.HALF_UP);

            dashboardVO.setAvgScore(avg);

        }else {
            dashboardVO.setAvgScore(BigDecimal.ZERO);
        }

        LambdaQueryWrapper<TransferStock> wrapper8 = new LambdaQueryWrapper<>();
        if (!isAdminOrHQ && currentStoreId != null){
            wrapper8.eq(TransferStock::getToStoreId,currentStoreId).or()
                    .eq(TransferStock::getFromStoreId,currentStoreId);

        }

        dashboardVO.setTotalTransfer(transferStockMapper.selectCount(wrapper8));


        LambdaQueryWrapper<User> wrapper9 = new LambdaQueryWrapper<>();

        if (!isAdminOrHQ && currentStoreId != null){
            wrapper9.eq(User::getStoreId,currentStoreId);
        }
        dashboardVO.setTotalUser(userMapper.selectCount(wrapper9));


        if ("GUIDE".equals(roleCode)){
            String realName = (String) StpUtil.getSession().get("realName");

            if (realName != null && currentStoreId != null){
                LambdaQueryWrapper<SalesAssist> wrapper = new LambdaQueryWrapper<>();

                wrapper.eq(SalesAssist::getName,realName).eq(SalesAssist::getStoreId,currentStoreId);
                SalesAssist salesAssist = salesAssistMapper.selectOne(wrapper);

                if (!Objects.isNull(salesAssist)){
                    dashboardVO.setGuideName(realName);
                    dashboardVO.setGuideSales(salesAssist.getSales());
                }
            }
        }

        if ("SUPERVISOR".equals(roleCode)){

            Object userId = StpUtil.getSession().get("userId");

            if (userId != null){

                LambdaQueryWrapper<StoreAudit> wrapper = new LambdaQueryWrapper<>();

                wrapper.eq(StoreAudit::getUserId,userId);

                List<StoreAudit> storeAuditList1 = storeAuditMapper.selectList(wrapper);

                List<SupervisorAuditItemVO> collect = storeAuditList1.stream().map(e -> {

                    SupervisorAuditItemVO supervisorAuditItemVO = new SupervisorAuditItemVO();

                    BeanUtils.copyProperties(e, supervisorAuditItemVO);

                    return supervisorAuditItemVO;

                }).collect(Collectors.toList());

                dashboardVO.setSupervisorAuditItemVOS(collect);


            }

        }


        return dashboardVO;
    }

    @Override
    public List<StoreRankingVO> getStoreRanking() {

        LambdaQueryWrapper<StorePerf> wrapper = new LambdaQueryWrapper<>();

        List<StorePerf> storePerfList = storePerfMapper.selectList(wrapper);


        Map<Long, BigDecimal> revenueMap = new java.util.HashMap<>();
        Map<Long, BigDecimal> targetMap = new java.util.HashMap<>();

        for (StorePerf perf : storePerfList) {
            Long storeId = perf.getStoreId();
            revenueMap.merge(storeId, perf.getRevenue() != null ? perf.getRevenue() : BigDecimal.ZERO, BigDecimal::add);
            targetMap.merge(storeId, perf.getTarget() != null ? perf.getTarget() : BigDecimal.ZERO, BigDecimal::add);
        }


        List<StoreRankingVO> collect = revenueMap.keySet().stream().map(storeId -> {
            StoreRankingVO storeRankingVO = new StoreRankingVO();

            storeRankingVO.setStoreId(storeId);

            Store store = storeMapper.selectById(storeId);

            if (store != null) {
                storeRankingVO.setStoreName(store.getName());
            }

            storeRankingVO.setRevenue(revenueMap.get(storeId));
            storeRankingVO.setTarget(targetMap.get(storeId));
            return storeRankingVO;
        }).sorted((a, b) -> {
            if (a.getRevenue() == null) {
                return 1;
            }
            if (b.getRevenue() == null) {
                return -1;
            }
            return b.getRevenue().compareTo(a.getRevenue());
        }).collect(Collectors.toList());




        return collect;
    }
}
