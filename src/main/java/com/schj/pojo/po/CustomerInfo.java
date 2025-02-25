package com.schj.pojo.po;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author AvA
 * @since 2025-02-21
 */
@Data
public class CustomerInfo{
    private Long id;

    private Long policyNo;

    private String customerType;

    private String idType;

    private String idNumber;

    private String fullName;

    private String gender;

    private String contactPhone;

    private String contactAddress;

    private Integer benefitRatio;

    private LocalDateTime createdTime;

    private String creator;

    private LocalDateTime updatedTime;

    private String updater;

    private Integer isDeleted;
}
