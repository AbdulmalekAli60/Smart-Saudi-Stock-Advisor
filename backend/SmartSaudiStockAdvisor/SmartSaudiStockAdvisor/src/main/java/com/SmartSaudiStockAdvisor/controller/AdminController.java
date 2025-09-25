package com.SmartSaudiStockAdvisor.controller;

import com.SmartSaudiStockAdvisor.dto.CompanyInformationDTO;
import com.SmartSaudiStockAdvisor.dto.CreateCompanyDTO;
import com.SmartSaudiStockAdvisor.dto.UserResponseDTO;
import com.SmartSaudiStockAdvisor.service.CompanyService;
import com.SmartSaudiStockAdvisor.service.ETagService;
import com.SmartSaudiStockAdvisor.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/admin")
public class AdminController {
    private final UserService userService;
    private final CompanyService companyService;
    private final ETagService eTagService;

    @Autowired
    public AdminController(UserService userService, CompanyService companyService, ETagService eTagService) {
        this.userService = userService;
        this.companyService = companyService;
        this.eTagService = eTagService;
    }

    @PostMapping(value = "/add-company")
    public ResponseEntity<CompanyInformationDTO> createCompany(@Valid @RequestBody CreateCompanyDTO createCompanyDTO){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(companyService.createCompany(createCompanyDTO));
    }

    @DeleteMapping(value = "/delete-company/{company-id}")
    public ResponseEntity<String> deleteCompany(@PathVariable(value = "company-id") Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(companyService.deleteCompany(id));
    }

    @GetMapping(value = "/all-users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(HttpServletRequest httpServletRequest){
        List<UserResponseDTO> allUsers = userService.getAllUsers();
        String currentETag = eTagService.generateETag(allUsers);
        String clientETag = httpServletRequest.getHeader("If-None-Match");

        if(currentETag.equals(clientETag)){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .cacheControl(CacheControl.maxAge(3, TimeUnit.MINUTES).mustRevalidate())
                .eTag(currentETag)
                .body(allUsers);
    }

    @DeleteMapping(value = "/delete-user/{user-id}")
    public ResponseEntity<String> deleteUser(@PathVariable(value = "user-id") Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.deleteUser(id));
    }

    @GetMapping(value = "/dashboard")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public  ResponseEntity<?> isAdmin(){
        return ResponseEntity.ok().build();
    }
}
