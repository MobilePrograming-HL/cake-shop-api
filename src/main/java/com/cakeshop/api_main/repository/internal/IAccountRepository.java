package com.cakeshop.api_main.repository.internal;

import com.cakeshop.api_main.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAccountRepository extends JpaRepository<Account, String> {

    Account findByUsername(String username);

    Account findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<Account> findAllByIsActiveFalse();
}
