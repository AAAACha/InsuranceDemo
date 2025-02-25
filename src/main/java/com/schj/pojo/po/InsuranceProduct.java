package com.schj.pojo.po;

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
public class InsuranceProduct{

    private Long id;

    private Integer productCode;

    private String productName;

    private String productCategory;

    private String durationType;

    private Integer paymentYears;

    private String paymentMethod;

    private String productStatus;

    private LocalDateTime createdTime;

    private String creator;

    private LocalDateTime updatedTime;

    private String updater;

    private Integer isDeleted;
}
