package com.schj.pojo.dto.request;

import lombok.Data;

/**
 * @author AvA
 * @since 2025-02-21
 */
@Data
public class InsuranceCompanyReqDTO {
    
    private Long id;

    private String companyCode;

    private String companyName;

    private String companyType;
}
