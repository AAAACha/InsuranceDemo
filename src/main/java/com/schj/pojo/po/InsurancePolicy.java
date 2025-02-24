package com.schj.pojo.po;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class InsurancePolicy {

    private Long id;

    private Long policyNo;

    private Long proposalNo;

    private String companyCode;

    private String companyName;

    private String policyStatus;

    private LocalDateTime applicationTime;

    private LocalDateTime effectiveTime;

    private BigDecimal premium;

    private BigDecimal insuredAmount;

    private String beneficiaryType;

    private Integer paymentYears;

    private String paymentMethod;

    private Integer paymentTimes;

    private String coveragePeriod;

    private LocalDateTime createdTime;

    private String creator;

    private LocalDateTime updatedTime;

    private String updater;

    private Integer isDeleted;
}
