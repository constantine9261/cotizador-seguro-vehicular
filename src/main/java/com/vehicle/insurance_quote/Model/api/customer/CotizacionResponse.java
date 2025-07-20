package com.vehicle.insurance_quote.Model.api.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CotizacionResponse {

    private BigDecimal primaBase;
    private List<String> ajustes;
    private BigDecimal primaTotal;
}
