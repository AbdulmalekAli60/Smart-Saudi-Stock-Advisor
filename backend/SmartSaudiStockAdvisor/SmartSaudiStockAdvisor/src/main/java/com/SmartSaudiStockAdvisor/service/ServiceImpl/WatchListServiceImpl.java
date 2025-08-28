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

    @Autowired
    public WatchListServiceImpl(WatchListRepo watchListRepo, UserRepo userRepo, CompanyRepo companyRepo) {
        this.watchListRepo = watchListRepo;
        this.userRepo = userRepo;
        this.companyRepo = companyRepo;
    }

    @Override
    @Transactional
    public String addToWatchList(Long userId, Long companyId) {
        boolean isWatchListExist = watchListRepo.existsByUserUserIdAndCompanyCompanyId(userId, companyId);

        if(!isWatchListExist){

            Company company = companyRepo.findById(companyId)
                    .orElseThrow(() -> new EntryNotFoundException("Company not found with ID: " + companyId));

            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new EntryNotFoundException("User not found with ID: " + userId));

                WatchList newWatchList = new WatchList(user, company);
                watchListRepo.save(newWatchList);
                log.info("New WatchList has been saved");
                return "New WatchList has been saved";
        }
        log.info("The company is Already in you Watch List");
        throw new AlreadyExistsException("The company is Already in your Watch List");
    }

    @Override
    @Transactional
    public String removeFromWatchList(Long watchListId) {

        WatchList watchList = watchListRepo.findById(watchListId)
                .orElseThrow(() -> new EntryNotFoundException("WatchList not found, Id: " + watchListId));

        Long currentUserId = getCurrentUserId();

        if (!Objects.equals(currentUserId, watchList.getUser().getUserId())) {
            throw new OperationFailedException("Access denied: You can only delete your own watchlists");
        }

        watchListRepo.deleteById(watchListId);
        log.info("WatchList deleted successfully. Id: {}", watchListId);
        return "WatchList deleted successfully. Id: " + watchListId;
    }

    private Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserPrincipal) {
            return ((UserPrincipal) principal).getUserId();
        }
        throw new OperationFailedException("User not authenticated");
    }

    @Override
    public List<WatchListResponseDTO> getWatchListsByUserId(Long userId) {
        return watchListRepo.findAllByUserUserId(userId)
                .stream()
                .map(WatchListResponseDTO::new)
                .toList();
    }
}
