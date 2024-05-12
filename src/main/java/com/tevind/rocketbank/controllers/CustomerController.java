package com.tevind.rocketbank.controllers;

import com.tevind.rocketbank.entities.Customer;
import com.tevind.rocketbank.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @PostMapping({"/api/customer/add", "/api/customer/add/"})
    public ResponseEntity<String> addUser(@RequestBody Customer customer) {
        try{
            customerService.createCustomer(customer);
            return new ResponseEntity<>("User added", HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println(e);
            return new ResponseEntity<>("Error adding user! " + e, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

    @GetMapping({"/api/customer", "/api/customer/"})
    public ResponseEntity<Customer> getCustomer() {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String uid = (String) authenticationToken.getToken().getClaims().get("sub");
        Customer customer = customerService.getCustomer(uid);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }
}
