package com.vehicle.insurance_quote.Model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("cotizaciones")
public class CotizacionEntity implements Serializable {

    @Id
    private Long id;

    private String marca;
    private String modelo;
    private int anio;
    private String tipoUso;
    private int edadConductor;

    private BigDecimal primaBase;
    private BigDecimal primaTotal;
    private LocalDateTime fechaCotizacion;

}
