package com.SmartSaudiStockAdvisor.controller;

import com.SmartSaudiStockAdvisor.dto.SectorResponseDTO;
import com.SmartSaudiStockAdvisor.service.SectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class SectorController {
    private final SectorService sectorService;

    @Autowired
    public SectorController(SectorService sectorService) {
        this.sectorService = sectorService;
    }

    @GetMapping(value = "/sectors")
    public ResponseEntity<List<SectorResponseDTO>> getSectors(){
        return ResponseEntity.status(HttpStatus.OK).body(sectorService.getAllSectors());
    }
}
