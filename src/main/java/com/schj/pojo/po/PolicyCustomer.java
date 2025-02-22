package com.schj.pojo.po;

import lombok.Data;

/**
 * @Descriptioin policy_customer
 * @Author AvA
 * @Date 2025-02-22
 */
@Data
public class PolicyCustomer {

    private Long policyId;

    private Long customerId;

    private String roleType;

    private Double beneficiaryRate;
}
