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

    void insertInsuranceProduct(InsuranceProductReqDTO insuranceProductReqDTO);

    InsuranceProductResDTO getInsuranceProductById(Long id);

    void updateInsuranceProductById(Long id, InsuranceProductReqDTO insuranceProductReqDTO);

    void deleteInsuranceProductById(Long id);
}
