package com.tevind.rocketbank.repositories;

import com.tevind.rocketbank.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomersRepository extends JpaRepository<Customer, String> {
}
