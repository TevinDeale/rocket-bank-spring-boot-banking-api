package com.tevind.rocketbank.repositories;

import com.tevind.rocketbank.entities.Account;
import com.tevind.rocketbank.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Iterable <Account>> findByCustomer(Customer customer);

}
