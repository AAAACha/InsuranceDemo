package com.schj.service.impl;

import com.schj.pojo.po.PolicyCustomer;
import com.schj.service.PolicyCustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Descriptioin PolicyCustomerServiceImpl
 * @Author AvA
 * @Date 2025-02-22
 */
@Service
public class PolicyCustomerServiceImpl implements PolicyCustomerService {
    @Override
    public void insertPolicyCustomer(PolicyCustomer policyCustomer) {

    }

    @Override
    public List<PolicyCustomer> getPolicyCustomerByPolicyId(Long id) {
        return List.of();
    }

    @Override
    public void updatePolicyCustomer(PolicyCustomer policyCustomer) {

    }

    @Override
    public void deletePolicyCustomerByPolicyId(Long id) {

    }

    @Override
    public List<Long> getCustomerIdListByPolicyId(Long id) {
        return List.of();
    }
}
