package com.SmartSaudiStockAdvisor.controller;

import com.SmartSaudiStockAdvisor.dto.HistoricalDataDTO;
import com.SmartSaudiStockAdvisor.service.ETagService;
import com.SmartSaudiStockAdvisor.service.HistoricalDataService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/historical-data")
public class HistoricalDataController {
    private final HistoricalDataService historicalDataService;
    private final ETagService eTagService;

    @Autowired
    public HistoricalDataController(HistoricalDataService historicalDataService, ETagService eTagService) {
        this.historicalDataService = historicalDataService;
        this.eTagService = eTagService;
    }

    @GetMapping(value = "/{company-id}")
    public ResponseEntity<List<HistoricalDataDTO>> getHistoricalData(@PathVariable(value = "company-id") Long companyId,
                                                                     HttpServletRequest httpServletRequest){

        List<HistoricalDataDTO> data = historicalDataService.getHistoricalDataByCompanyId(companyId);
        String currentETag = eTagService.generateETag(data);
        String clientETag = httpServletRequest.getHeader("If-None-Match");

        if(currentETag.equals(clientETag)){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }

        return ResponseEntity.status(HttpStatus.OK)
                .cacheControl(CacheControl.maxAge(24, TimeUnit.HOURS))
                .eTag(currentETag)
                .body(data);
    }
}
