package com.schj.pojo.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Descriptioin InsuranceCompanyResDTO
 * @Author AvA
 * @Date 2025-02-21
 */
@Data
public class InsuranceCompanyResDTO {

    private Long id;

    private String companyCode;

    private String companyName;

    private String companyType;

    private LocalDateTime createdTime;

    private String creator;

    private LocalDateTime updatedTime;

    private String updater;

    private Integer isDeleted;
}
