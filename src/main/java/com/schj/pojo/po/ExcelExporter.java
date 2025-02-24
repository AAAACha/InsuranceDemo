package com.schj.pojo.po;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Descriptioin ExcelExporter
 * @Author AvA
 * @Date 2025-02-24
 */
@Data
public class ExcelExporter {
    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("保单号")
    private Long policyNo;

    @ExcelProperty("投保单号")
    private Long proposalNo;

    @ExcelProperty("保险公司编码")
    private String companyCode;

    @ExcelProperty("保险公司名称")
    private String companyName;

    @ExcelProperty("险种代码")
    private String productCode;

    @ExcelProperty("险种名称")
    private String productName;

    @ExcelProperty("投保人")
    private String policyholder;

    @ExcelProperty("被保人")
    private String insured;

    @ExcelProperty("受保人")
    private String beneficiary;

    @ExcelProperty("保单状态")
    private String policyStatus;

    @ExcelProperty("投保时间")
    private LocalDateTime applicationTime;

    @ExcelProperty("生效时间")
    private LocalDateTime effectiveTime;

    @ExcelProperty("保费")
    private BigDecimal premium;

    @ExcelProperty("保额")
    private BigDecimal insuredAmount;

    private String beneficiaryType;

    @ExcelProperty("缴费年限")
    private Integer paymentYears;

    @ExcelProperty("缴费方式")
    private String paymentMethod;

    @ExcelProperty("缴费次数")
    private Integer paymentTimes;

    @ExcelProperty("保障期间")
    private String coveragePeriod;

    @ExcelProperty("创建时间")
    private LocalDateTime createdTime;

    @ExcelProperty("创建人")
    private String creator;

    @ExcelProperty("更新时间")
    private LocalDateTime updatedTime;

    @ExcelProperty("更新人")
    private String updater;

    @ExcelProperty("是否删除")
    private Integer isDeleted;
}
