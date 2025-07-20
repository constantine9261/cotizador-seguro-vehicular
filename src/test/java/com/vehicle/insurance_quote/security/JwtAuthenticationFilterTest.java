package com.vehicle.insurance_quote.security;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebFilter;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(controllers = JwtAuthenticationFilterTest.DummyController.class)
class JwtAuthenticationFilterTest {

    @Autowired
    private WebTestClient webTestClient;

    @RestController
    static class DummyController {
        @GetMapping("/test")
        public String test() {
            return "OK";
        }
    }





    @Test
    void shouldRejectRequestWithInvalidToken() {
        webTestClient.get()
                .uri("/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer invalid.token")
                .exchange()
                .expectStatus().isUnauthorized();
    }


}