package com.SmartSaudiStockAdvisor.service.ServiceImpl;

import com.SmartSaudiStockAdvisor.dto.InvestAmountDTO;
import com.SmartSaudiStockAdvisor.dto.UpdateAccountDetailsDTO;
import com.SmartSaudiStockAdvisor.dto.UserResponseDTO;
import com.SmartSaudiStockAdvisor.entity.User;
import com.SmartSaudiStockAdvisor.exception.AlreadyExistsException;
import com.SmartSaudiStockAdvisor.exception.EntryNotFoundException;
import com.SmartSaudiStockAdvisor.exception.OperationFailedException;
import com.SmartSaudiStockAdvisor.exception.UpdateInvestmentAmountException;
import com.SmartSaudiStockAdvisor.repo.UserRepo;
import com.SmartSaudiStockAdvisor.security.UserPrincipal;
import com.SmartSaudiStockAdvisor.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    @Autowired
    public UserServiceImpl(UserRepo repo, BCryptPasswordEncoder passwordEncoder, MessageSource source) {
        this.userRepo = repo;
        this.passwordEncoder = passwordEncoder;
        this.messageSource = source;
    }

    @Override
    @Transactional
    public String updateInvestAmount(Long userId, InvestAmountDTO investAmountDTO) {
        Long[] notFoundParam = {userId};
        User user = userRepo.findById(userId)
                .orElseThrow(() -> {
            log.info("User was not found to update invest amount. passed Id: {}", userId);
                    return new EntryNotFoundException(getMessage("user-service.update-invest-amount.user.not-found-message", notFoundParam));
        });

        try {
            user.setInvestAmount(investAmountDTO.getInvestAmount());
            log.info("Invest amount has been updated to {}, for user: {}", investAmountDTO.getInvestAmount(), userId);
            userRepo.save(user);
            return getMessage("user-service.update-invest-amount.success-message", null);
        }catch (DataAccessException e){
            log.error("Database error during updating investment amount for user: {}", userId, e);
            throw new UpdateInvestmentAmountException(getMessage("user-service.update-invest-amount.failed-message", null));
        }
    }

    @Override
    public UserResponseDTO freshUserInfo() {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(user instanceof UserPrincipal){
            Optional<User> currentUser = userRepo.findByUsername(((UserPrincipal) user).getUsername());
            return new UserResponseDTO(currentUser.get(), "this is info");
        }

        throw new OperationFailedException("Error happened") ;
    }

    @Override
    public List<UserResponseDTO> getAllUsers() { // for admin
        return userRepo.findAll()
                .stream()
                .map(UserResponseDTO::new)
                .toList();
    }

    @Override
    @Transactional
    public String deleteUser(Long userId) { // for admin
        Long[] idParam = {userId};
        User currentUser = userRepo.findById(userId)
                .orElseThrow(() -> new EntryNotFoundException(getMessage("user-service.delete-user.not-found-message", idParam)));

        try {
            userRepo.delete(currentUser);
            log.info("User {} with ID: {} has been deleted", currentUser.getName(), userId);
            Object[] successParams = {currentUser.getName(), currentUser.getUserId()};
            return getMessage("user-service.delete-user.success-message", successParams);
        } catch (DataIntegrityViolationException e) {
            Object[] failParams = {currentUser.getName(), e};
            throw new OperationFailedException(getMessage("user-service.delete-user.fail-message", failParams));
        }
    }

    @Override
    @Transactional
    public UserResponseDTO updateUserInformation(UpdateAccountDetailsDTO updateAccountDetailsDTO) {
        String username  = SecurityContextHolder.getContext().getAuthentication().getName();

        String[] usernameParam = {updateAccountDetailsDTO.getUsername()};
        User currentUser = userRepo.findByUsername(username)
                .orElseThrow(() -> new EntryNotFoundException(getMessage("user-service.update-info.username.not-found-message", usernameParam)));

        try{
            if(updateAccountDetailsDTO.getName() != null){
                currentUser.setName(updateAccountDetailsDTO.getName());
            }

            if(updateAccountDetailsDTO.getUsername() != null){
                if(!updateAccountDetailsDTO.getUsername().equals(currentUser.getUsername())) {
                    boolean isUsernameUsed = userRepo.existsByUsername(updateAccountDetailsDTO.getUsername());
                    if(isUsernameUsed){
                        throw new AlreadyExistsException(getMessage("user-service.update-info.username.already-taken", null));
                    }
                }
                currentUser.setUsername(updateAccountDetailsDTO.getUsername());
            }

            if (updateAccountDetailsDTO.getEmail() != null){
                if(!updateAccountDetailsDTO.getEmail().equals(currentUser.getEmail())){
                    boolean isEmailUsed = userRepo.existsByEmail(updateAccountDetailsDTO.getEmail());
                    if(isEmailUsed){
                        throw new AlreadyExistsException(getMessage("user-service.update-info.email.already-taken", null));
                    }
                }
                currentUser.setEmail(updateAccountDetailsDTO.getEmail());
            }

            if(updateAccountDetailsDTO.getPassword() != null){
                currentUser.setPassword(passwordEncoder.encode(updateAccountDetailsDTO.getPassword()));
            }

            User updatedUser = userRepo.save(currentUser);
            log.info("User has been updated");
            return new UserResponseDTO(updatedUser, getMessage("user-service.update-info.success-message", null));
        }catch (DataAccessException e){
            String[] errorParam = {e.getMessage()};
            throw new OperationFailedException(getMessage("user-service.update-info.fail-message", errorParam));
        }
    }

    @Override
    @Transactional
    public String deleteAccount() { // for user
        Object userPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(userPrincipal instanceof UserPrincipal){
            Long userId = ((UserPrincipal) userPrincipal).getUserId();

                userRepo.deleteById(userId);
                return getMessage("user-service.delete-account.success-message", null);
        }
        throw new OperationFailedException(getMessage("user-service.delete-account.fail-message", null));
    }

    private String getMessage(String key, Object[] params){
        return messageSource.getMessage(key, params, LocaleContextHolder.getLocale());
    }
}
