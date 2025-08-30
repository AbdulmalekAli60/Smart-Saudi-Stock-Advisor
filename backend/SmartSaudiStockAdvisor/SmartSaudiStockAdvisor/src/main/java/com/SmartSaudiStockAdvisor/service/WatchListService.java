package com.SmartSaudiStockAdvisor.service;

import com.SmartSaudiStockAdvisor.dto.WatchListResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WatchListService {
    String addToWatchList(Long userId, Long companyId);

    String removeFromWatchList(Long watchListId);

    List<WatchListResponseDTO> WatchListsForCurrentUser();
}
