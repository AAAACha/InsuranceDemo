package com.schj.service;

import com.schj.pojo.po.PolicyCustomer;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Descriptioin PolicyCustomerService
 * @Author AvA
 * @Date 2025-02-22
 */
@Service
public interface PolicyCustomerService {

    // 插入新的 PolicyCustomer 记录
    void insertPolicyCustomer(PolicyCustomer policyCustomer);

    // 根据 PolicyID 获取 PolicyCustomer 记录
    List<PolicyCustomer> getPolicyCustomerByPolicyId(Long id);

    // 更新 PolicyCustomer 记录
    void updatePolicyCustomer(PolicyCustomer policyCustomer);

    // 根据 PolicyID 删除 PolicyCustomer 记录
    void deletePolicyCustomerByPolicyId(Long id);

    List<Long> getCustomerIdListByPolicyId(Long id);
}
