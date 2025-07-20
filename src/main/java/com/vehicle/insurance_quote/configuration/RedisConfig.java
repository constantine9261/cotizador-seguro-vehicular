package com.vehicle.insurance_quote.configuration;

import com.vehicle.insurance_quote.Model.api.customer.CotizacionRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

    @Bean(name = "customReactiveRedisTemplate")
    public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        RedisSerializationContext<String, String> context = RedisSerializationContext
                .<String, String>newSerializationContext(RedisSerializer.string())
                .value(RedisSerializer.string())
                .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

    private String generateKey(CotizacionRequest request) {
        return String.format("COTIZACION:%s:%s:%d:%s:%d",
                request.getMarca(),
                request.getModelo(),
                request.getAnio(),
                request.getTipoUso(),
                request.getEdadConductor());
    }
}
