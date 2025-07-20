package com.vehicle.insurance_quote.controller;

import com.vehicle.insurance_quote.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@WebFluxTest(AuthController.class)
@ImportAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration.class
})
class AuthControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testLoginReturnsToken() {
        String username = "testuser";

        webTestClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/auth/login")
                                .queryParam("username", username)
                                .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody(Map.class)
                .value(body -> {
                    assertThat(body).containsKey("token");
                    String token = (String) body.get("token");
                    assertThat(token).isNotBlank();
                    assertThat(JwtUtil.validateToken(token)).isEqualTo(username);
                });
    }
}