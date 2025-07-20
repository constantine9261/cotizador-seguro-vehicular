package com.vehicle.insurance_quote.Model.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vehicle.insurance_quote.Model.api.customer.CotizacionRequest;
import com.vehicle.insurance_quote.Model.api.customer.CotizacionResponse;
import com.vehicle.insurance_quote.Model.entity.CotizacionEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CotizacionMapper {

    private static final ObjectMapper mapper = new ObjectMapper();


    public static CotizacionResponse fromJson(String json) {
        try {
            return mapper.readValue(json, CotizacionResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al deserializar CotizacionResponse", e);
        }
    }

    public static String toJson(CotizacionResponse response) {
        try {
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            throw new RuntimeException("Error al serializar CotizacionResponse", e);
        }
    }

    public static CotizacionEntity toEntity(CotizacionRequest req, BigDecimal primaBase, BigDecimal primaTotal) {
        return CotizacionEntity.builder()
                .marca(req.getMarca())
                .modelo(req.getModelo())
                .anio(req.getAnio())
                .tipoUso(req.getTipoUso())
                .edadConductor(req.getEdadConductor())
                .primaBase(primaBase)
                .primaTotal(primaTotal)
                .fechaCotizacion(LocalDateTime.now())
                .build();
    }

    public static CotizacionResponse toResponse(BigDecimal primaBase, List<String> ajustes, BigDecimal primaTotal) {
        return CotizacionResponse.builder()
                .primaBase(primaBase)
                .ajustes(ajustes)
                .primaTotal(primaTotal)
                .build();
    }
}
