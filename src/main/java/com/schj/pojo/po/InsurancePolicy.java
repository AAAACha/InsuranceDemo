package com.schj.pojo.po;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author AvA
 * @since 2025-02-21
 */
@Data
public class InsurancePolicy implements Serializable {

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
}
