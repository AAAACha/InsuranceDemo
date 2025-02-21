package com.schj.pojo.po;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author AvA
 * @since 2025-02-21
 */
@Data
public class InsuranceCompany{

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
