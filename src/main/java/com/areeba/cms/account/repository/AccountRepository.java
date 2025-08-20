package com.areeba.cms.account.repository;

import java.util.UUID;

import com.areeba.cms.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

}
