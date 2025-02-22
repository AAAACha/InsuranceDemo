package com.schj.pojo.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class PolicyQueryRequest {

    // 保单号
    private String policyNo;

    // 保司名称
    private String companyName;

    // 险种名称
    private String productName;

    // 投保时间范围 - 开始时间
    private Date startApplicationTime;

    // 投保时间范围 - 结束时间
    private Date endApplicationTime;

    // 生效时间范围 - 开始时间
    private Date startEffectiveTime;

    // 生效时间范围 - 结束时间
    private Date endEffectiveTime;

    // 当前页码，默认值为 1
    private Integer pageNum = 1;

    // 每页大小，默认值为 10
    private Integer pageSize = 10;
}