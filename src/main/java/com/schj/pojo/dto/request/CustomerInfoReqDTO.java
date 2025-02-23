package com.schj.pojo.dto.request;

import lombok.Data;

/**
 * @Descriptioin CustomerInfoReqDTO
 * @Author AvA
 * @Date 2025-02-21
 */
@Data
public class CustomerInfoReqDTO {

    private Long policyId;

    private String customerType;

    private String idType;

    private String idNumber;

    private String fullName;

    private String gender;

    private String contactPhone;

    private String contactAddress;

    private Integer benefitRatio;
}