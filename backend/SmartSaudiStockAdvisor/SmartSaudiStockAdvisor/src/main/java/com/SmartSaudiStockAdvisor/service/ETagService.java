package com.SmartSaudiStockAdvisor.service;

import org.springframework.stereotype.Service;

@Service
public interface ETagService {

    String generateETag(Object data);
}
