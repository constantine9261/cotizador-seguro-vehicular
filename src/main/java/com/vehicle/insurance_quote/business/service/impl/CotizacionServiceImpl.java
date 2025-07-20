package com.vehicle.insurance_quote.business.service.impl;

import com.vehicle.insurance_quote.Model.Utils.CotizacionMapper;
import com.vehicle.insurance_quote.Model.api.customer.CotizacionRequest;
import com.vehicle.insurance_quote.Model.api.customer.CotizacionResponse;
import com.vehicle.insurance_quote.Model.entity.CotizacionEntity;
import com.vehicle.insurance_quote.business.repository.CotizacionRepository;
import com.vehicle.insurance_quote.business.service.ICotizacionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

@Service
@Slf4j
public class CotizacionServiceImpl implements ICotizacionService {

    private CotizacionRepository cotizacionRepository;
    private final ReactiveRedisTemplate<String, String> redisTemplate; // 🔴 Este es el que debes tener


    public CotizacionServiceImpl(
            CotizacionRepository cotizacionRepository,
            @Qualifier("customReactiveRedisTemplate") ReactiveRedisTemplate<String, String> redisTemplate
    ) {
        this.cotizacionRepository = cotizacionRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<CotizacionResponse> cotizar(CotizacionRequest request) {
        String redisKey = "cotizacion:" + request.hashCode(); // ejemplo simple
        ReactiveValueOperations<String, String> ops = redisTemplate.opsForValue();

        return ops.get(redisKey)
                .flatMap(json -> Mono.just(CotizacionMapper.fromJson(json))) // cache hit
                .switchIfEmpty(
                        calcularCotizacion(request)
                                .flatMap(res -> {
                                    String json = CotizacionMapper.toJson(res);
                                    return ops.set(redisKey, json, Duration.ofMinutes(5))
                                            .thenReturn(res);
                                })
                );
    }

    private Mono<CotizacionResponse> calcularCotizacion(CotizacionRequest request) {

        BigDecimal primaBase = BigDecimal.valueOf(500);

        // Clase record para definir reglas
        record Regla(String descripcion, boolean aplica, BigDecimal porcentaje) {}

        List<Regla> reglas = List.of(
                new Regla("Año > 2015 (+15%)", request.getAnio() > 2015, BigDecimal.valueOf(0.15)),
                new Regla("Uso: carga (+10%)", "carga".equalsIgnoreCase(request.getTipoUso()), BigDecimal.valueOf(0.10)),
                new Regla("Edad > 50 (-5%)", request.getEdadConductor() > 50, BigDecimal.valueOf(-0.05)),
                new Regla("Marca BMW (+20%)", "bmw".equalsIgnoreCase(request.getMarca()), BigDecimal.valueOf(0.20)),
                new Regla("Marca Audi (+10%)", "audi".equalsIgnoreCase(request.getMarca()), BigDecimal.valueOf(0.10))
        );

        return Flux.fromIterable(reglas)
                .filter(Regla::aplica)
                .collectList()
                .flatMap(reglasAplicadas -> {
                    List<String> ajustes = reglasAplicadas.stream()
                            .map(Regla::descripcion)
                            .toList();

                    BigDecimal primaTotal = reglasAplicadas.stream()
                            .map(Regla::porcentaje)
                            .map(p -> primaBase.multiply(p))
                            .reduce(primaBase, BigDecimal::add);

                    // Guardamos en la base de datos
                    CotizacionEntity entity = CotizacionMapper.toEntity(request, primaBase, primaTotal);

                    return cotizacionRepository.save(entity)
                            .thenReturn(CotizacionMapper.toResponse(primaBase, ajustes, primaTotal));
                });
    }

}
