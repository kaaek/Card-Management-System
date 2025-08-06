package com.example.cms.repository;

import java.util.UUID;

import com.example.cms.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    /*
    Get out of the box: save(Account account) (creates and updates), findById(UUID id), findAll(), deleteById(UUID id), existsById(UUID id), count()
    */

}
