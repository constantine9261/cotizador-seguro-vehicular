package com.vehicle.insurance_quote.Model.api.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CotizacionRequest {

    private String marca;
    private String modelo;
    private int anio;
    private String tipoUso;
    private int edadConductor;
}
