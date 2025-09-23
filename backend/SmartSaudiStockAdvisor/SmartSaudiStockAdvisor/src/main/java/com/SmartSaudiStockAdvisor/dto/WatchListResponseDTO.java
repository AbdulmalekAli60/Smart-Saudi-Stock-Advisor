package com.SmartSaudiStockAdvisor.dto;

import com.SmartSaudiStockAdvisor.entity.WatchList;

public class WatchListResponseDTO {
    private Long watchListId;
    private Long userId;
    private Long companyId;
    private String companyLogo;
    private String companyName;
    private String tickerName;

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
        this.companyLogo = watchList.getCompany().getCompanyLogo();
        this.companyName = watchList.getCompany().getCompanyArabicName();
        this.tickerName = watchList.getCompany().getTickerName();
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTickerName() {
        return tickerName;
    }

    public void setTickerName(String tickerName) {
        this.tickerName = tickerName;
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
