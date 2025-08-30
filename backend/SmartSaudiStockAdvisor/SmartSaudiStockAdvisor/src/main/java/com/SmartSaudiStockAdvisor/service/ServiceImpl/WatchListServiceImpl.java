package com.SmartSaudiStockAdvisor.service.ServiceImpl;

import com.SmartSaudiStockAdvisor.dto.WatchListResponseDTO;
import com.SmartSaudiStockAdvisor.entity.Company;
import com.SmartSaudiStockAdvisor.entity.User;
import com.SmartSaudiStockAdvisor.entity.WatchList;
import com.SmartSaudiStockAdvisor.exception.AlreadyExistsException;
import com.SmartSaudiStockAdvisor.exception.EntryNotFoundException;
import com.SmartSaudiStockAdvisor.exception.OperationFailedException;
import com.SmartSaudiStockAdvisor.repo.CompanyRepo;
import com.SmartSaudiStockAdvisor.repo.UserRepo;
import com.SmartSaudiStockAdvisor.repo.WatchListRepo;
import com.SmartSaudiStockAdvisor.security.UserPrincipal;
import com.SmartSaudiStockAdvisor.service.WatchListService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class WatchListServiceImpl implements WatchListService {

    private final WatchListRepo watchListRepo;
    private final UserRepo userRepo;
    private final CompanyRepo companyRepo;
    private final MessageSource messageSource;

    @Autowired
    public WatchListServiceImpl(WatchListRepo watchListRepo, UserRepo userRepo, CompanyRepo companyRepo, MessageSource source) {
        this.watchListRepo = watchListRepo;
        this.userRepo = userRepo;
        this.companyRepo = companyRepo;
        this.messageSource = source;
    }

    @Override
    @Transactional
    public String addToWatchList(Long userId, Long companyId) {
        boolean isWatchListExist = watchListRepo.existsByUserUserIdAndCompanyCompanyId(userId, companyId);

        if(!isWatchListExist){
            Long[] companyIdParam = {companyId};
            Company company = companyRepo.findById(companyId)
                    .orElseThrow(() -> new EntryNotFoundException(getMessage("watch-list-service.company.not-found-message", companyIdParam)));

            Long[] userIdParam = {userId};
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new EntryNotFoundException(getMessage("watch-list-service.user.not-found-message", userIdParam)));

                WatchList newWatchList = new WatchList(user, company);
                watchListRepo.save(newWatchList);
                log.info("New WatchList has been saved");
                return getMessage("watch-list-service.add.success-message", null);
        }
        log.info("The company is Already in you Watch List");
        throw new AlreadyExistsException(getMessage("watch-list-service.already-added", null));
    }

    @Override
    @Transactional
    public String removeFromWatchList(Long watchListId) {

        Long[] watchListIdParam = {watchListId};
        WatchList watchList = watchListRepo.findById(watchListId)
                .orElseThrow(() -> new EntryNotFoundException(getMessage("watch-list-service.remove.not-found-message", watchListIdParam)));

        Long currentUserId = getCurrentUserId();

        if (!Objects.equals(currentUserId, watchList.getUser().getUserId())) {
            throw new OperationFailedException(getMessage("watch-list-service.remove.not-yours",null));
        }

        watchListRepo.deleteById(watchListId);
        log.info("WatchList deleted successfully. Id: {}", watchListId);
        return getMessage("watch-list-service.remove.success-message", null);
    }

    @Override
    public List<WatchListResponseDTO> WatchListsForCurrentUser() {
        return watchListRepo.findAllByUserUserId(getCurrentUserId())
                .stream()
                .map(WatchListResponseDTO::new)
                .toList();
    }

    private String getMessage(String key, Object[] params){
        return messageSource.getMessage(key, params, LocaleContextHolder.getLocale());
    }

    private Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserPrincipal) {
            return ((UserPrincipal) principal).getUserId();
        }
        throw new OperationFailedException(getMessage("watch-list-service.getCurrentUserId.fail-message", null));
    }
}
