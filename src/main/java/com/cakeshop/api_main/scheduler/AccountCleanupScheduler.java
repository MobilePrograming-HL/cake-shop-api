package com.cakeshop.api_main.scheduler;

import com.cakeshop.api_main.model.Account;
import com.cakeshop.api_main.repository.internal.IAccountRepository;
import com.cakeshop.api_main.service.redis.IRedisService;
import com.cakeshop.api_main.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AccountCleanupScheduler {
    private final long intervalTime = 5 * 60 * 1000; // 5 minutes

    @Autowired
    private IAccountRepository accountRepository;

    @Autowired
    private IRedisService redisService;

    @Scheduled(fixedRate = intervalTime)
    public void removeInactiveAccountsWithoutOtp() {
        log.info("Starting cleanup of inactive accounts without OTP");

        List<Account> inactiveAccounts = accountRepository.findAllByIsActiveFalse();
        List<String> accountIdsToDelete = new ArrayList<>();

        for (Account account : inactiveAccounts) {
            String otpKey = RedisUtils.getRedisKeyForConfirmEmail(account.getEmail());
            boolean otpExists = redisService.hasKey(otpKey);

            if (!otpExists) {
                log.info("Marking account for deletion: {}", account.getEmail());
                accountIdsToDelete.add(account.getId());
            }
        }

        if (!accountIdsToDelete.isEmpty()) {
            accountRepository.deleteAllByIdInBatch(accountIdsToDelete);
            log.info("Finished cleanup. Total accounts deleted: {}", accountIdsToDelete.size());
        } else {
            log.info("No accounts to delete in this cycle.");
        }
    }
}
