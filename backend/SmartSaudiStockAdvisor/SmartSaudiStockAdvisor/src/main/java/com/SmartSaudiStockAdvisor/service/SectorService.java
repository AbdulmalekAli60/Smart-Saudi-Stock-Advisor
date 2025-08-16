package com.SmartSaudiStockAdvisor.service;

import com.SmartSaudiStockAdvisor.dto.SectorResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SectorService {
    List<SectorResponseDTO> getAllSectors();
}
