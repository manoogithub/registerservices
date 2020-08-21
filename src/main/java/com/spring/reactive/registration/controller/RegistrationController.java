package com.spring.reactive.registration.controller;

import com.spring.reactive.registration.document.Customer;
import com.spring.reactive.registration.document.ExceptionEntity;
import com.spring.reactive.registration.document.LoginEntity;

import com.spring.reactive.registration.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityResultHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import reactor.core.publisher.Mono;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
public class RegistrationController  {


    @Autowired
    RegistrationRepository registrationRepository;


    @PostMapping("/v1/controller/create")
    public Mono<ResponseEntity<String>> createRegister(@Valid @RequestBody Customer customer) {


        return registrationRepository.save(customer)
                .map(customer2 -> new ResponseEntity<>("successfully Registered", HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>("Not Registered", HttpStatus.NOT_FOUND));

    }

    @PutMapping("/v1/controller/update/{userId}")
    public Mono<ResponseEntity<Customer>> updateItem(@PathVariable String userId, @RequestBody Customer customer) {

        return registrationRepository.findById(userId)
                .flatMap(existingCustomer -> {
                    existingCustomer.setAddress(customer.getAddress());
                    existingCustomer.setContactNumber(customer.getContactNumber());
                    existingCustomer.setEmail(customer.getEmail());
                    return registrationRepository.save(existingCustomer);
                })
                .map(updatedCustomer -> new ResponseEntity<>(updatedCustomer, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PostMapping("/v1/controller/login")
    public ResponseEntity<?> autenticateUser(@RequestBody LoginEntity loginEntity) {
        HashMap<String, String> map = new HashMap<>();
        try {
            registrationRepository.findByUserIdAndPassword(loginEntity.getLoginUser(), loginEntity.getLoginPassword());
            map.put("userId",loginEntity.getLoginUser());
            map.put("bearerToken",getToken(loginEntity.getLoginUser(),loginEntity.getLoginPassword()));
            map.put("result", "UserSingedInSuccesfully");
            return new ResponseEntity<Map>(map, HttpStatus.OK);
        } catch (Exception e) {
            map.put("result","Invalid credentials");
            return new ResponseEntity<Map>(map,HttpStatus.UNAUTHORIZED);
        }
    }

    // Generate JWT token
    public String getToken(String username, String password) throws Exception {

        String jwtToken = null;
        jwtToken = Jwts.builder().setSubject(username).signWith(SignatureAlgorithm.HS256, "secret").compact();
        return jwtToken;

    }
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders headers, HttpStatus status, WebRequest request) {
        ExceptionEntity exceptionResponse = new ExceptionEntity(new Date(), "Validation Failed",
                ex.getBindingResult().toString());
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
