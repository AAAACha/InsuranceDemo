package com.schj.pojo.dto.response;

import com.schj.pojo.po.CustomerInfo;
import com.schj.pojo.po.InsuranceProduct;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Descriptioin InsurancePolicyResDTO
 * @Author AvA
 * @Date 2025-02-22
 */
@Data
public class InsurancePolicyResDTO {
    private Long id;

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

    private LocalDateTime createdTime;

    private String creator;

    private LocalDateTime updatedTime;

    private String updater;

    private Integer isDeleted;

    private List<CustomerInfo> customerInfoList;

    private List<InsuranceProduct> insuranceProductList;
}
