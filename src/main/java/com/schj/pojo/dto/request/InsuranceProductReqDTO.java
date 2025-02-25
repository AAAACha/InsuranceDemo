package com.schj.pojo.dto.request;

import lombok.Data;

/**
 * @author AvA
 * @since 2025-02-21
 */
@Data
public class InsuranceProductReqDTO {

    /**
     * 险种代码
     */
    private String productCode;

    /**
     * 险种名称
     */
    private String productName;

    /**
     * 主附险标识
     */
    private String productCategory;

    /**
     * 长短险标识
     */
    private String durationType;

    /**
     * 缴费年限
     */
    private Integer paymentYears;

    /**
     * 缴费方式
     */
    private String paymentMethod;

    /**
     * 险种状态
     */
    private String productStatus;
}
