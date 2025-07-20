package com.vehicle.insurance_quote.Model.api.shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<T> {

    private T data; // Datos de la respuesta
    private String message; // Mensaje sobre el estado de la operación
    private String status; // Estado de la respuesta (ej. "SUCCESS" o "ERROR")
}
