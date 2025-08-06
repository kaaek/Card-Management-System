package com.example.cms;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    /*
    Get out of the box: save(Account account) (creates and updates), findById(UUID id), findAll(), deleteById(UUID id), existsById(UUID id), count()
    */

}
