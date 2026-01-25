package com.SmartSaudiStockAdvisor.dto;

import com.SmartSaudiStockAdvisor.entity.WatchList;

public record WatchListResponseDTO(Long watchListId,
                                   Long userId,
                                   Long companyId,
                                   String companyLogo,
                                   String companyName,
                                   String tickerName
                                  ) {
    public WatchListResponseDTO(WatchList watchList) {
        this(
                watchList.getWatchListId(),
                watchList.getUser().getUserId(),
                watchList.getCompany().getCompanyId(),
                watchList.getCompany().getCompanyLogo(),
                watchList.getCompany().getCompanyArabicName(),
                watchList.getCompany().getTickerName()
        );
    }
}
