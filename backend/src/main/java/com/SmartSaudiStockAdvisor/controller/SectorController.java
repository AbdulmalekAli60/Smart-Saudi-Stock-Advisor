package com.SmartSaudiStockAdvisor.controller;

import com.SmartSaudiStockAdvisor.dto.SectorResponseDTO;
import com.SmartSaudiStockAdvisor.service.ETagService;
import com.SmartSaudiStockAdvisor.service.SectorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping(value = "/sector")
public class SectorController {
    private final SectorService sectorService;
    private final ETagService eTagService;

    @Autowired
    public SectorController(SectorService sectorService, ETagService eTagService) {
        this.sectorService = sectorService;
        this.eTagService = eTagService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<SectorResponseDTO>> getSectors(HttpServletRequest httpServletRequest){
        List<SectorResponseDTO> sectors = sectorService.getAllSectors();
        String currentETag = eTagService.generateETag(sectors);
        String clientETag = httpServletRequest.getHeader("If-None-Match");

        if(currentETag.equals(clientETag)){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.status(HttpStatus.OK)
                .cacheControl(CacheControl.maxAge(7, TimeUnit.DAYS).mustRevalidate())
                .eTag(currentETag)
                .body(sectors);
    }
}
