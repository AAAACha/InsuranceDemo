package com.schj.service;

import com.schj.pojo.dto.request.InsurancePolicyReqDTO;
import com.schj.pojo.dto.response.InsurancePolicyResDTO;
import org.springframework.stereotype.Service;

/**
 * @author AvA
 * @since 2025-02-21
 */
@Service
public interface InsurancePolicyService {

    void insertInsurancePolicy(InsurancePolicyReqDTO insurancePolicyReqDTO);

    InsurancePolicyResDTO getInsurancePolicyById(Long id);

    void updateInsurancePolicyById(Long id, InsurancePolicyReqDTO insurancePolicyReqDTO);

    void deleteInsurancePolicyById(Long id);
}
