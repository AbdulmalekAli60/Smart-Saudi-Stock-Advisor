package com.SmartSaudiStockAdvisor.service.ServiceImpl;

import com.SmartSaudiStockAdvisor.dto.PredictionResponseDTO;
import com.SmartSaudiStockAdvisor.entity.Prediction;
import com.SmartSaudiStockAdvisor.exception.PredictionNotFoundException;
import com.SmartSaudiStockAdvisor.repo.PredictionRepo;
import com.SmartSaudiStockAdvisor.service.PredictionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PredictionServiceImpl implements PredictionService {

    private final PredictionRepo predictionRepo;
    private final MessageSource messageSource;

    @Autowired
    public PredictionServiceImpl(PredictionRepo predictionRepo, MessageSource source) {
        this.predictionRepo = predictionRepo;
        this.messageSource = source;
    }

    @Override
    public List<PredictionResponseDTO> getPredictionsByCompanyId(Long id) {
        return predictionRepo.findByCompanyCompanyIdOrderByPredictionDateAsc(id)
                .stream()
                .map(PredictionResponseDTO::new)
                .toList();
    }

    @Override
    public PredictionResponseDTO getLatestPredictionByCompanyId(Long companyId) {
        Prediction prediction = predictionRepo.findTop1ByCompanyCompanyIdOrderByPredictionDateDesc(companyId);

        if(prediction != null){
            return new PredictionResponseDTO(prediction);
        }
        Long[] param = {companyId};
        log.error("Prediction was not Found for Company: {}", companyId);
        throw new PredictionNotFoundException(getMessage("prediction-service.company.not-found-message", param));
    }

    private String getMessage(String key, Object[] params){
        return messageSource.getMessage(key, params, LocaleContextHolder.getLocale());
    }
}
