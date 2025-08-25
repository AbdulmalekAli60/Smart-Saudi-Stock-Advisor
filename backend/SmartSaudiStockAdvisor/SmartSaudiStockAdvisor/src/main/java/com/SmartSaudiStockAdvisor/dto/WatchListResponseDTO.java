package com.SmartSaudiStockAdvisor.dto;

import com.SmartSaudiStockAdvisor.entity.WatchList;

public class WatchListResponseDTO {
    private Long watchListId;
    private Long userId;
    private Long companyId;

    public WatchListResponseDTO() {
    }

    public WatchListResponseDTO(Long watchListId, Long userId, Long companyId) {
        this.watchListId = watchListId;
        this.userId = userId;
        this.companyId = companyId;
    }

    public WatchListResponseDTO(WatchList watchList) {
        this.watchListId = watchList.getWatchListId();
        this.userId = watchList.getUser().getUserId();
        this.companyId = watchList.getCompany().getCompanyId();
    }

    public Long getWatchListId() {
        return watchListId;
    }

    public void setWatchListId(Long watchListId) {
        this.watchListId = watchListId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
