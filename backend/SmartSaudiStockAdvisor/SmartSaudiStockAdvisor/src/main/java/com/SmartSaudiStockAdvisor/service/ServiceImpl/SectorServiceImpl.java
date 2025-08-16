package com.SmartSaudiStockAdvisor.service.ServiceImpl;

import com.SmartSaudiStockAdvisor.dto.SectorResponseDTO;
import com.SmartSaudiStockAdvisor.repo.SectorRepo;
import com.SmartSaudiStockAdvisor.service.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectorServiceImpl implements SectorService {

    private final SectorRepo sectorRepo;

    @Autowired
    public SectorServiceImpl(SectorRepo sectorRepo) {
        this.sectorRepo = sectorRepo;
    }

    @Override
    public List<SectorResponseDTO> getAllSectors() {
        return sectorRepo.findAll().stream()
                .map(SectorResponseDTO::new)
                .toList();
    }
}
