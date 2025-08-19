package com.SmartSaudiStockAdvisor.controller;

import com.SmartSaudiStockAdvisor.dto.PredictionResponseDTO;
import com.SmartSaudiStockAdvisor.service.ETagService;
import com.SmartSaudiStockAdvisor.service.PredictionService;
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
@RequestMapping(value = "/predictions")
public class PredictionController {
    private final PredictionService predictionService;
    private final ETagService eTagService;

    @Autowired
    public PredictionController(PredictionService predictionService, ETagService eTagService) {
        this.predictionService = predictionService;
        this.eTagService = eTagService;
    }

    @GetMapping(value = "/{company-id}/all")
    public ResponseEntity<List<PredictionResponseDTO>> getAllPredationsCompanyId(@PathVariable(value = "company-id") Long companyId,
                                                                                 HttpServletRequest httpServletRequest){
        List<PredictionResponseDTO> predictions = predictionService.getPredictionsByCompanyId(companyId);
        String currentETag = eTagService.generateETag(predictions);
        String clientETag = httpServletRequest.getHeader("If-None-Match");

        if(currentETag.equals(clientETag)){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }

        return ResponseEntity.status(HttpStatus.OK)
                .cacheControl(CacheControl.maxAge(24, TimeUnit.HOURS))
                .eTag(currentETag)
                .body(predictions);
    }

    @GetMapping(value = "/latest/{company-id}")
    public ResponseEntity<PredictionResponseDTO> getLatestPrediction(@PathVariable(value = "company-id") Long companyId,
                                                                                 HttpServletRequest httpServletRequest){
        PredictionResponseDTO responseDTO = predictionService.getLatestPredictionByCompanyId(companyId);
        String currentETag = eTagService.generateETag(responseDTO);
        String clientETag = httpServletRequest.getHeader("If-None-Match");

        if(currentETag.equals(clientETag)){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }

        return ResponseEntity.status(HttpStatus.OK)
                .cacheControl(CacheControl.maxAge(24, TimeUnit.HOURS))
                .eTag(currentETag)
                .body(responseDTO);
    }
}
