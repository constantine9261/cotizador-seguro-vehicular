package com.vehicle.insurance_quote.controller;

import com.vehicle.insurance_quote.Model.api.customer.CotizacionRequest;
import com.vehicle.insurance_quote.Model.api.customer.CotizacionResponse;
import com.vehicle.insurance_quote.business.service.ICotizacionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@WebFluxTest(controllers = CotizacionController.class)
@Import({CustomerControllerTest.TestSecurityConfig.class, CustomerControllerTest.MockConfig.class})
class CustomerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ICotizacionService cotizacionService;

    @Test
    void testCotizarEndpoint() {
        CotizacionResponse response = CotizacionResponse.builder()
                .primaBase(BigDecimal.valueOf(500))
                .primaTotal(BigDecimal.valueOf(575))
                .ajustes(List.of("Año > 2015 (+15%)"))
                .build();

        Mockito.when(cotizacionService.cotizar(Mockito.any()))
                .thenReturn(Mono.just(response));

        CotizacionRequest request = CotizacionRequest.builder()
                .marca("Toyota")
                .modelo("Corolla")
                .anio(2020)
                .tipoUso("particular")
                .edadConductor(30)
                .build();

        webTestClient.post()
                .uri("/customers/cotizaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.primaTotal").isEqualTo(575.0);
    }

    // ✅ Mock del servicio
    @TestConfiguration
    static class MockConfig {
        @Bean
        public ICotizacionService cotizacionService() {
            return Mockito.mock(ICotizacionService.class);
        }
    }

    // ✅ Seguridad abierta solo para pruebas
    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
            return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                    .authorizeExchange(exchanges -> exchanges.anyExchange().permitAll())
                    .build();
        }
    }
}