package com.tevind.rocketbank.service;

import com.tevind.rocketbank.entities.Customer;
import com.tevind.rocketbank.repositories.CustomersRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    private final CustomersRepository customersRepository;
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);


    @Autowired
    public CustomerService(CustomersRepository customersRepository) {
        this.customersRepository = customersRepository;
    }

    @Transactional
    public Customer createCustomer(Customer customer) {
        logger.info("Creating customer: {}", customer.getFullName());
        return customersRepository.save(customer);
    }

    public Customer getCustomer(String uuid) {
        logger.info("Finding customer by UUID: {}", uuid);
        return customersRepository.findById(uuid).orElse(null);
    }

    @Transactional
    public Customer updateCustomer(Customer customer) {
        logger.info("Updating Customer: {}", customer.getFullName());
        return customersRepository.save(customer);
    }

    @Transactional
    public void deleteCustomer(String uuid) {
        Optional<Customer> customer = customersRepository.findById(uuid);
        logger.info("Deleting Customer: {}", customer.get().getFullName());
        customersRepository.deleteById(uuid);
    }
}
