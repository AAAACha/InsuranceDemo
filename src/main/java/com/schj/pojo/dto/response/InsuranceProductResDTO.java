package com.schj.pojo.dto.response;

import lombok.Data;

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
public class InsuranceProductResDTO {
    private Long id;

    private String productCode;

    private String productName;

    private String productCategory;

    private String durationType;

    private String productStatus;

    private LocalDateTime createdTime;

    private String creator;

    private LocalDateTime updatedTime;

    private String updater;

    private Integer isDeleted;
}
