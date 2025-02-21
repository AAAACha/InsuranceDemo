package com.schj.service;


import com.schj.pojo.dto.request.InsuranceProductReqDTO;
import com.schj.pojo.dto.response.InsuranceProductResDTO;
import org.springframework.stereotype.Service;

/**
 * @author AvA
 * @since 2025-02-21
 */
@Service
public interface InsuranceProductService {

    Boolean insertInsuranceProduct(InsuranceProductReqDTO insuranceProductReqDTO);

    InsuranceProductResDTO getInsuranceProductById(Long id);

    Boolean updateInsuranceProductById(InsuranceProductReqDTO insuranceProductReqDTO);

    Boolean deleteInsuranceProductById(Long id);
}
