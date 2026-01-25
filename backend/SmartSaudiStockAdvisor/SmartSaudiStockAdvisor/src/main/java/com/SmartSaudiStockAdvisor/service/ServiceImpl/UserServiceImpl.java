package com.SmartSaudiStockAdvisor.service.ServiceImpl;

import com.SmartSaudiStockAdvisor.dto.UpdateAccountDetailsDTO;
import com.SmartSaudiStockAdvisor.dto.UserResponseDTO;
import com.SmartSaudiStockAdvisor.entity.User;
import com.SmartSaudiStockAdvisor.exception.AlreadyExistsException;
import com.SmartSaudiStockAdvisor.exception.EntryNotFoundException;
import com.SmartSaudiStockAdvisor.exception.OperationFailedException;
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
    public UserResponseDTO freshUserInfo() {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user instanceof UserPrincipal) {
            // It is safer to use orElseThrow here instead of .get() directly
            User currentUser = userRepo.findByUsername(((UserPrincipal) user).getUsername())
                    .orElseThrow(() -> new EntryNotFoundException("Current user not found in database"));

            return new UserResponseDTO(currentUser, "this is info");
        }

        throw new OperationFailedException("Error happened");
    }

    @Override
    public List<UserResponseDTO> getAllUsers() { // for admin
        return userRepo.findAll()
                .stream()
                .map(UserResponseDTO::new) // This works if your Record has the constructor (User user)
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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Note: Using record accessor .username() instead of .getUsername()
        String[] usernameParam = {updateAccountDetailsDTO.username()};

        User currentUser = userRepo.findByUsername(username)
                .orElseThrow(() -> new EntryNotFoundException(getMessage("user-service.update-info.username.not-found-message", usernameParam)));

        try {
            if (updateAccountDetailsDTO.name() != null) {
                currentUser.setName(updateAccountDetailsDTO.name());
            }

            if (updateAccountDetailsDTO.username() != null) {
                if (!updateAccountDetailsDTO.username().equals(currentUser.getUsername())) {
                    boolean isUsernameUsed = userRepo.existsByUsername(updateAccountDetailsDTO.username());
                    if (isUsernameUsed) {
                        throw new AlreadyExistsException(getMessage("user-service.update-info.username.already-taken", null));
                    }
                }
                currentUser.setUsername(updateAccountDetailsDTO.username());
            }

            if (updateAccountDetailsDTO.email() != null) {
                if (!updateAccountDetailsDTO.email().equals(currentUser.getEmail())) {
                    boolean isEmailUsed = userRepo.existsByEmail(updateAccountDetailsDTO.email());
                    if (isEmailUsed) {
                        throw new AlreadyExistsException(getMessage("user-service.update-info.email.already-taken", null));
                    }
                }
                currentUser.setEmail(updateAccountDetailsDTO.email());
            }

            if (updateAccountDetailsDTO.password() != null) {
                currentUser.setPassword(passwordEncoder.encode(updateAccountDetailsDTO.password()));
            }

            if (updateAccountDetailsDTO.investAmount() != null) {
                currentUser.setInvestAmount(updateAccountDetailsDTO.investAmount());
            }

            User updatedUser = userRepo.save(currentUser);
            log.info("User has been updated");
            return new UserResponseDTO(updatedUser, getMessage("user-service.update-info.success-message", null));

        } catch (DataAccessException e) {
            String[] errorParam = {e.getMessage()};
            throw new OperationFailedException(getMessage("user-service.update-info.fail-message", errorParam));
        }
    }

    @Override
    @Transactional
    public String deleteAccount() { // for user
        Object userPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userPrincipal instanceof UserPrincipal) {
            Long userId = ((UserPrincipal) userPrincipal).getUserId();
            userRepo.deleteById(userId);
            return getMessage("user-service.delete-account.success-message", null);
        }
        throw new OperationFailedException(getMessage("user-service.delete-account.fail-message", null));
    }

    private String getMessage(String key, Object[] params) {
        return messageSource.getMessage(key, params, LocaleContextHolder.getLocale());
    }
}