package com.SmartSaudiStockAdvisor.controller;

import com.SmartSaudiStockAdvisor.dto.InvestAmountDTO;
import com.SmartSaudiStockAdvisor.dto.UserResponseDTO;
import com.SmartSaudiStockAdvisor.service.ETagService;
import com.SmartSaudiStockAdvisor.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;
    private final ETagService eTagService;

    @Autowired
    public UserController(UserService userService, ETagService eTagService) {
        this.userService = userService;
        this.eTagService = eTagService;
    }

    @PatchMapping(value = "investment-amount/{id}")
    public ResponseEntity<String> updateInvestAmount(@PathVariable(name = "id") Long userId, @Valid @RequestBody InvestAmountDTO investAmountDTO){
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateInvestAmount(userId, investAmountDTO));
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(HttpServletRequest httpServletRequest){
        List<UserResponseDTO> allUsers = userService.getAllUsers();
        String currentETag = eTagService.generateETag(allUsers);
        String clientETag = httpServletRequest.getHeader("If-None-Match");

        if(currentETag.equals(clientETag)){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .cacheControl(CacheControl.maxAge(3, TimeUnit.MINUTES))
                .eTag(currentETag)
                .body(allUsers);
    }

    @DeleteMapping(value = "/delete/{user-id}")
    public ResponseEntity<String> deleteUser(@PathVariable(value = "user-id") Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.deleteUser(id));
    }
}
