package com.spring.reactive.registration.router;

import com.spring.reactive.registration.handler.RegistrationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RegistrationRouter {

    @Bean
    public RouterFunction<ServerResponse> loan(RegistrationHandler registrationHandler) {


        return RouterFunctions.route(POST("/v1/function/create").and(accept(MediaType.APPLICATION_JSON)), registrationHandler::createCustomer)
                .andRoute(POST("v1/function/login").and(accept(MediaType.APPLICATION_JSON)), registrationHandler::userLogin)
                .andRoute(PUT("v1/function/update/{userId}").and(accept(MediaType.APPLICATION_JSON)), registrationHandler::update);

    }
}
