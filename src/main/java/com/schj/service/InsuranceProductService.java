package com.schj.service;


import com.schj.pojo.dto.request.InsuranceProductReqDTO;
import com.schj.pojo.po.Result;
import org.springframework.stereotype.Service;

/**
 * @author AvA
 * @since 2025-02-21
 */
@Service
public interface InsuranceProductService {

    Result insertInsuranceProduct(InsuranceProductReqDTO insuranceProductReqDTO);

    Result getInsuranceProductById(Long id);

    Result updateInsuranceProductById(Long id, InsuranceProductReqDTO insuranceProductReqDTO);

    Result deleteInsuranceProductById(Long id);
}
