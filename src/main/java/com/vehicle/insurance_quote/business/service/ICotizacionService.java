package com.vehicle.insurance_quote.business.service;


import com.vehicle.insurance_quote.Model.api.customer.CotizacionRequest;
import com.vehicle.insurance_quote.Model.api.customer.CotizacionResponse;
import reactor.core.publisher.Mono;

public interface ICotizacionService {

    Mono<CotizacionResponse> cotizar(CotizacionRequest request);

}