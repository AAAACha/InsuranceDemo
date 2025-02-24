package com.schj.pojo.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.schj.pojo.po.CustomerInfo;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Descriptioin InsurancePolicyReqDTO
 * @Author AvA
 * @Date 2025-02-22
 */
@Data
public class InsurancePolicyReqDTO {

    /**
     * 保险单号码
     * 用于唯一标识一份保险合同
     */
    private String policyNo;

    /**
     * 投保单号码
     * 用于唯一标识一份投保申请
     */
    private String proposalNo;

    /**
     * 公司代码
     * 用于标识承保公司的代码
     */
    private String companyCode;

    /**
     * 公司名字
     * 承保公司的名字
     */
    private String companyName;

    /**
     * 产品代码列表
     * 包含了该保险单涵盖的所有产品代码
     */
    private List<Long> productIdList;

    /**
     * 保险单状态
     * 描述了保险单当前的状态，如有效、失效等
     */
    private String policyStatus;

    /**
     * 投保申请时间
     * 记录了客户提交投保申请的时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime applicationTime;

    /**
     * 保险生效时间
     * 指保险合同开始生效的时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime effectiveTime;

    /**
     * 保费
     * 代表客户需支付的保险费用总额
     */
    private BigDecimal premium;

    /**
     * 保险金额
     * 表示保险合同中约定的最高赔偿限额
     */
    private BigDecimal insuredAmount;

    /**
     * 受益人类型(法定/指定)
     */
    private String beneficiaryType;

    /**
     * 缴费年数
     * 描述客户需要缴纳保费的年限
     */
    private Integer paymentYears;

    /**
     * 缴费方式
     * 描述客户缴纳保费的方式，如一次性付清、分期付款等
     */
    private String paymentMethod;

    /**
     * 缴费次数
     * 表示客户需要缴纳保费的总次数
     */
    private Integer paymentTimes;

    /**
     * 保障期间
     * 描述保险合同的有效期间
     */
    private String coveragePeriod;

    /**
     * 客户信息列表
     * 包含了所有与该保险单相关的客户信息
     */
    private List<CustomerInfo> customerInfoList;
}
