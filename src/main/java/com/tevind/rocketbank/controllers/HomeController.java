package com.tevind.rocketbank.controllers;

import com.tevind.rocketbank.entities.Customer;
import com.tevind.rocketbank.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private CustomerService customerService;

    public HomeController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping({"/home", "/home/"})
    public String getMessage() {
        logger.info("Running the Home Controller");
        return "Welcome Home!";
    }

    @GetMapping("/hello")
    public ResponseEntity<String> getHello() {
        logger.info("Running the Hello Controller");
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String uid = (String) authenticationToken.getToken().getClaims().get("sub");
        Customer customer = customerService.getCustomer(uid);
        String name = customer.getFullName();
        String username = customer.getUsername();
        String response = "Hello, " + name + ", welcome to your api!\n" + "Your UUID: " + uid + "\nUsername: " + username;
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
