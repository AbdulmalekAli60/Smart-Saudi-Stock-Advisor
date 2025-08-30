package com.SmartSaudiStockAdvisor.controller;

import com.SmartSaudiStockAdvisor.dto.WatchListResponseDTO;
import com.SmartSaudiStockAdvisor.security.UserPrincipal;
import com.SmartSaudiStockAdvisor.service.ETagService;
import com.SmartSaudiStockAdvisor.service.WatchListService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping(value = "/watch-list")
public class WatchListController {

    private final WatchListService watchListService;
    private final ETagService eTagService;

    @Autowired
    public WatchListController(WatchListService watchListService, ETagService eTagService) {
        this.watchListService = watchListService;
        this.eTagService = eTagService;
    }

    @PostMapping(value = "/add/{company-id}")
    public ResponseEntity<String> addWatchList(@PathVariable(value = "company-id") Long companyId,
                                               @AuthenticationPrincipal UserPrincipal userPrincipal){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(watchListService.addToWatchList(userPrincipal.getUserId(), companyId));
    }

    @DeleteMapping(value = "/delete/{watch-list-id}")
    public ResponseEntity<String> deleteWatchList(@PathVariable(value = "watch-list-id") Long watchListId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(watchListService.removeFromWatchList(watchListId));
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<WatchListResponseDTO>> watchListsForUser(HttpServletRequest httpServletRequest){
        List<WatchListResponseDTO> listResponseDTOS = watchListService.WatchListsForCurrentUser();
        String clinetETage = httpServletRequest.getHeader("If-None-Match");
        String currentETag = eTagService.generateETag(listResponseDTOS);

        if(currentETag.equals(clinetETage)){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }

        return ResponseEntity.status(HttpStatus.OK)
                .cacheControl(CacheControl.maxAge(15,TimeUnit.MINUTES)
                        .cachePrivate()
                        .mustRevalidate())
                .eTag(currentETag)
                .body(listResponseDTOS);
    }
}
