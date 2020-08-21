package com.spring.reactive.registration.handler;


import com.spring.reactive.registration.document.Customer;
import com.spring.reactive.registration.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class RegistrationHandler {
    @Autowired
    RegistrationRepository registrationRepository;
    static Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    public Mono<ServerResponse> createCustomer(ServerRequest serverRequest) {

        Mono<Customer> newCustomer = serverRequest.bodyToMono(Customer.class);

      /*  return newCustomer.flatMap(customer -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(registrationRepository.save(customer), Customer.class));
*/
       return newCustomer.flatMap(p-> {

            return registrationRepository.save(p);
        }).flatMap(p -> ServerResponse.created(URI.create("/v1/function/create".concat(p.getUserId())))
                .contentType(APPLICATION_JSON)
                .syncBody(p))
                .onErrorResume(error -> {
                    WebClientResponseException errorResponse = (WebClientResponseException) error;
                    if(errorResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        return ServerResponse.badRequest()
                                .contentType(APPLICATION_JSON)
                                .syncBody(errorResponse.getResponseBodyAsString());
                    }
                    return Mono.error(errorResponse);
                });

    }

    public Mono<ServerResponse> userLogin(ServerRequest serverRequest) {
        Mono<Customer> existCustomer = serverRequest.bodyToMono(Customer.class);

        return errorHandler(existCustomer.flatMap(login -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(registrationRepository.findByUserIdAndPassword(login.getUserId(), login.getPassword()), Customer.class)
                .switchIfEmpty(ServerResponse.notFound().build())));




    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("userId");

        Mono<Customer> update = serverRequest.bodyToMono(Customer.class)
                .flatMap(updateCustomer -> {
                    Mono<Customer> existUser = registrationRepository.findById(id)
                            .flatMap(currentCustomer -> {
                                currentCustomer.setAddress(updateCustomer.getAddress());
                                currentCustomer.setContactNumber(updateCustomer.getContactNumber());
                                currentCustomer.setEmail(updateCustomer.getEmail());

                                return registrationRepository.save(currentCustomer);
                            });
                    return existUser;
                });

        return update.flatMap(updated -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON).body(fromObject(updated)).switchIfEmpty(notFound));

    }

    private Mono<ServerResponse> errorHandler(Mono<ServerResponse> response){
        return response.onErrorResume(error -> {
            WebClientResponseException errorResponse = (WebClientResponseException) error;
            if(errorResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                Map<String, Object> body = new HashMap<>();
                body.put("error", "Not found: ".concat(errorResponse.getMessage()));
                body.put("timestamp", new Date());
                body.put("status", errorResponse.getStatusCode().value());
                return ServerResponse.status(HttpStatus.NOT_FOUND).syncBody(body);
            }
            return Mono.error(errorResponse);
        });
    }
}
