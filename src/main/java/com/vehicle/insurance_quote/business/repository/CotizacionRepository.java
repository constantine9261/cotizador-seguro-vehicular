package com.vehicle.insurance_quote.business.repository;


import com.vehicle.insurance_quote.Model.entity.CotizacionEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CotizacionRepository extends
        ReactiveCrudRepository<CotizacionEntity, Long> {

}
