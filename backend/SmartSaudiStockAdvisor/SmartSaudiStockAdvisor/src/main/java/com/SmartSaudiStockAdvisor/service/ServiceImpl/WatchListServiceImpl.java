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
import com.SmartSaudiStockAdvisor.service.WatchListService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public String removeFromWatchList(Long watchListId) {
        boolean isWatchListExist = watchListRepo.existsById(watchListId);

        if(!isWatchListExist){
            throw new EntryNotFoundException("The WatchList dose not Exists, Id: " + watchListId);
        }

        try{
            watchListRepo.deleteById(watchListId);
            log.info("WatchList Deleted Successfully. Id: {}", watchListId);
            return "WatchList Deleted Successfully";
        }catch (DataAccessException e){
            log.info("Could not delete watch list. Id: {}", watchListId);
            throw new OperationFailedException("Failed to delete watch list: " + e.getMessage());
        }

    }

    @Override
    public List<WatchListResponseDTO> getWatchListsByUserId(Long userId) {
        return watchListRepo.findAllByUserUserId(userId)
                .stream()
                .map(WatchListResponseDTO::new)
                .toList();
    }
}
