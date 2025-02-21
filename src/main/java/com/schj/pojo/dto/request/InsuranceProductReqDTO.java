package com.schj.pojo.dto.request;

import lombok.Data;

/**
 * @author AvA
 * @since 2025-02-21
 */
@Data
public class InsuranceProductReqDTO {

    private Long id;

    private String productCode;

    private String productName;

    private String productCategory;

    private String durationType;

    private String productStatus;
}
