package com.schj.pojo.dto.request;

import com.schj.pojo.po.CustomerInfo;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Descriptioin InsurancePolicyReqDTO
 * @Author AvA
 * @Date 2025-02-22
 */
@Data
public class InsurancePolicyReqDTO {

    private String policyNo;

    private String proposalNo;

    private String companyCode;

    private String productCode;

    private String policyStatus;

    private LocalDateTime applicationTime;

    private LocalDateTime effectiveTime;

    private BigDecimal premium;

    private BigDecimal insuredAmount;

    private Integer paymentYears;

    private String paymentMethod;

    private Integer paymentTimes;

    private String coveragePeriod;

    private List<CustomerInfo> policyholderList;

    private List<CustomerInfo> insuredList;

    private List<CustomerInfo> beneficiaryList;
}
