package com.ojbk.service;

import com.ojbk.vo.DashboardVO;
import com.ojbk.vo.StoreRankingVO;

import java.util.List;

public interface DashboardService {

    DashboardVO getDashboardVO();


    List<StoreRankingVO> getStoreRanking();
}
