package com.vehicle.insurance_quote.business.service.impl;

import com.vehicle.insurance_quote.Model.api.customer.CotizacionRequest;
import com.vehicle.insurance_quote.Model.entity.CotizacionEntity;
import com.vehicle.insurance_quote.business.repository.CotizacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Duration;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CotizacionServiceImplTest {

    private CotizacionRepository cotizacionRepository;
    private ReactiveRedisTemplate<String, String> redisTemplate;
    private ReactiveValueOperations<String, String> valueOperations;

    private CotizacionServiceImpl cotizacionService;

    @BeforeEach
    void setUp() {
        cotizacionRepository = Mockito.mock(CotizacionRepository.class);
        redisTemplate = Mockito.mock(ReactiveRedisTemplate.class);
        valueOperations = Mockito.mock(ReactiveValueOperations.class);

        // Simula comportamiento del redisTemplate
        Mockito.when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // Simula que no hay nada en caché (Mono.empty())
        Mockito.when(valueOperations.get(anyString()))
                .thenReturn(Mono.empty());

        // Simula guardar en cache exitosamente (usa Duration)
        Mockito.when(valueOperations.set(anyString(), anyString(), any(Duration.class)))
                .thenReturn(Mono.just(true));

        // Instancia manual del servicio con dependencias simuladas
        cotizacionService = new CotizacionServiceImpl(cotizacionRepository, redisTemplate);
    }

    @Test
    void testCotizar() {
        CotizacionRequest request = CotizacionRequest.builder()
                .marca("Toyota")
                .modelo("Corolla")
                .anio(2020)
                .tipoUso("particular")
                .edadConductor(30)
                .build();

        CotizacionEntity savedEntity = new CotizacionEntity();
        savedEntity.setPrimaTotal(BigDecimal.valueOf(1000.00)); // valor simulado para evitar null

        Mockito.when(cotizacionRepository.save(any()))
                .thenReturn(Mono.just(savedEntity));

        StepVerifier.create(cotizacionService.cotizar(request))
                .expectNextMatches(res ->
                        res.getPrimaBase().compareTo(new BigDecimal("500")) == 0 &&
                        res.getPrimaTotal().compareTo(new BigDecimal("575.00")) == 0 &&
                        res.getAjustes().contains("Año > 2015 (+15%)"))
                .verifyComplete();
    }
}