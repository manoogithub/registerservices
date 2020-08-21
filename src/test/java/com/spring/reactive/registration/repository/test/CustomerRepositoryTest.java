package com.spring.reactive.registration.repository.test;

import com.spring.reactive.registration.document.Customer;
import com.spring.reactive.registration.repository.RegistrationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
@ExtendWith(SpringExtension.class)
public class CustomerRepositoryTest {
    @Autowired
    private RegistrationRepository registrationRepository;
    List<Customer> customerList = Arrays.asList(
            new Customer("1", "mano1994", "manoj", "kumar", "morgoni", "chennai", "tamilnadu", "india", "manoj@gmail", "wgfcsdhg45464", 9940048859L, "2020-05-05", "saving")
            , new Customer("2", "mano1994", "manoj1", "kumar", "morgoni", "chennai", "tamilnadu", "india", "manoj@gmail", "wgfcsdhg45464", 9940048859L, "2020-05-05", "saving")
            , new Customer("3", "mano1994", "manoj2", "kumar", "morgoni", "chennai", "tamilnadu", "india", "manoj@gmail", "wgfcsdhg45464", 9940048859L, "2020-05-05", "saving")

    );

    @BeforeEach
    public void setUpData() {
        registrationRepository.deleteAll()
                .thenMany(Flux.fromIterable(customerList))
                .flatMap(registrationRepository::save)
                .doOnNext((item -> {
                    System.out.println("inserted item is " + item);
                }))
                .blockLast();
    }

    @Test
    public void saveTest(){
        Customer newcustiteam =new Customer("4", "mano1994", "manoj", "kumar", "morgoni", "chennai", "tamilnadu", "india", "manoj@gmail", "wgfcsdhg45464", 9940048859L, "2020-05-05", "saving");

        Mono<Customer> saveCustomer = registrationRepository.save(newcustiteam);
        StepVerifier.create(saveCustomer)
                .expectSubscription()
                .expectNextMatches(customer->customer.getFirstName().equalsIgnoreCase("kumar")&& customer.getUserId()!=null)
                .verifyComplete();

    }
    @Test
    public void loginTest(){
        StepVerifier.create(registrationRepository.findByUserIdAndPassword("1","mano1994"))
        .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public  void updateTest(){
        Mono<Customer> updateCustomer =registrationRepository.findByUserIdAndPassword("1","mano1994")
        .map(customer -> {
            customer.setContactNumber(9565265644L);
            return customer;
        }).flatMap(customer1->{
            return registrationRepository.save(customer1);
        });
        StepVerifier.create(updateCustomer)
                .expectSubscription()
                .expectNextMatches(customer -> customer.getContactNumber()==9565265644L)
                .verifyComplete();
    }

}


