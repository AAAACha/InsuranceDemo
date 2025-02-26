package com.schj.service;

import com.schj.pojo.dto.request.InsurancePolicyReqDTO;
import com.schj.pojo.dto.request.PolicyQueryRequest;
import com.schj.pojo.po.Result;
import org.springframework.stereotype.Service;

/**
 * @author AvA
 * @since 2025-02-21
 */
@Service
public interface InsurancePolicyService {

    Result insertInsurancePolicy(InsurancePolicyReqDTO insurancePolicyReqDTO);

    Result getInsurancePolicyById(Long id);

    Result updateInsurancePolicyById(Long id, InsurancePolicyReqDTO insurancePolicyReqDTO);

    Result deleteInsurancePolicyById(Long id);

    Result getPoliciesByCondition(PolicyQueryRequest policyQueryRequest);
}
