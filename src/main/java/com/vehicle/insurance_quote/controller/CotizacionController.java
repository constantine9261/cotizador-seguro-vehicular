package com.vehicle.insurance_quote.controller;


import com.vehicle.insurance_quote.Model.api.customer.CotizacionRequest;
import com.vehicle.insurance_quote.Model.api.customer.CotizacionResponse;
import com.vehicle.insurance_quote.Model.api.shared.ResponseDto;
import com.vehicle.insurance_quote.Model.api.shared.ResponseDtoBuilder;
import com.vehicle.insurance_quote.business.service.ICotizacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Slf4j
public class CotizacionController {

    @Autowired
    private ICotizacionService cotizacionService;

    @Operation(
            summary = "Cotiza un seguro vehicular",
            description = "Calcula la prima total y los ajustes aplicados según las reglas de negocio definidas"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cotización generada con éxito"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/cotizaciones")
    public Mono<ResponseDto<CotizacionResponse>> cotizar(@RequestBody Mono<CotizacionRequest> request) {
        return request
                .flatMap(cotizacionService::cotizar)
                .map(result -> ResponseDtoBuilder.success(result, "Cotización generada con éxito"))
                .onErrorResume(ex -> {
                    log.error("Error al cotizar: {}", ex.getMessage(), ex);
                    return Mono.just(ResponseDtoBuilder.error("Error al procesar la cotización"));
                });
    }
}
