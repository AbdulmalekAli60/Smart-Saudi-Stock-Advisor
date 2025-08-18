package com.SmartSaudiStockAdvisor.service.ServiceImpl;

import com.SmartSaudiStockAdvisor.service.ETagService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ETagServiceImpl implements ETagService {

    private final ObjectMapper objectMapper;

    @Autowired
    public ETagServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String generateETag(Object data) {
        try{
            String json = objectMapper.writeValueAsString(data);
            log.info("ETag generated Successfully for class: {}", data.getClass().getSimpleName());
            return "\"" + Math.abs(json.hashCode()) + "\"";
        } catch (Exception e) {
            log.error("Error: {} while generating ETag with json, defaulted to current time for class: {}.", e.getMessage() ,data.getClass().getSimpleName());
            return "\"" + System.currentTimeMillis() + "\"";
        }
    }
}
