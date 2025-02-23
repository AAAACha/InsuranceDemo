package com.schj.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.schj.mapper.CustomerInfoMapper;
import com.schj.mapper.InsurancePolicyMapper;
import com.schj.mapper.InsuranceProductMapper;
import com.schj.pojo.dto.request.InsurancePolicyReqDTO;
import com.schj.pojo.dto.request.PolicyQueryRequest;
import com.schj.pojo.dto.response.InsurancePolicyResDTO;
import com.schj.pojo.po.*;
import com.schj.service.InsurancePolicyService;
import com.schj.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Descriptioin insurancePolicyServiceImpl
 * @Author AvA
 * @Date 2025-02-22
 */
@Service
public class InsurancePolicyServiceImpl implements InsurancePolicyService {

    @Autowired
    InsurancePolicyMapper insurancePolicyMapper;

    @Autowired
    CustomerInfoMapper customerInfoMapper;

    @Autowired
    InsuranceProductMapper insuranceProductMapper;

    @Override
    public void insertInsurancePolicy(InsurancePolicyReqDTO insurancePolicyReqDTO) {

        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(1);
        long id = snowflakeIdWorker.nextId();

        InsurancePolicy insurancePolicy = BeanUtil.toBean(insurancePolicyReqDTO, InsurancePolicy.class);

        insurancePolicy.setCreator("admin");
        insurancePolicy.setCreatedTime(LocalDateTime.now());
        insurancePolicy.setUpdater("admin");
        insurancePolicy.setUpdatedTime(LocalDateTime.now());

        insurancePolicyMapper.insertInsurancePolicy(insurancePolicy);

        //获得被保人客户列表
        List<CustomerInfo> insuredList = insurancePolicyReqDTO.getInsuredList();
        //获得投保人客户列表
        List<CustomerInfo> policyholderList = insurancePolicyReqDTO.getPolicyholderList();
        //获得受保人客户列表
        List<CustomerInfo> beneficiaryList = insurancePolicyReqDTO.getBeneficiaryList();

        for (CustomerInfo customerInfo : insuredList) {
            customerInfoMapper.insertCustomerInfoService(customerInfo);
        }

        for (CustomerInfo customerInfo : policyholderList) {
            customerInfoMapper.insertCustomerInfoService(customerInfo);
        }

        int benefitRatio = 0;
        for (CustomerInfo customerInfo : beneficiaryList) {
            benefitRatio += customerInfo.getBenefitRatio();
        }
        if(benefitRatio == 100){
            for (CustomerInfo customerInfo : beneficiaryList) {
                customerInfoMapper.insertCustomerInfoService(customerInfo);
            }
        }

    }

    /**
     * 根据保险单ID获取保险单详情
     * 此方法首先从数据库中检索指定ID的保险单信息，然后获取与该保险单关联的客户和产品信息，
     * 最后将这些信息封装到一个DTO中返回这主要用于展示保险单的详细信息，包括投保客户和包含的产品
     * @param id 保险单的唯一标识符
     * @return 包含保险单详细信息的DTO对象
     */
    @Override
    public InsurancePolicyResDTO getInsurancePolicyById(Long id) {
        // 根据ID从数据库中获取保险单信息
        InsurancePolicy insurancePolicy = insurancePolicyMapper.getInsurancePolicyById(id);

        // 将保险单实体转换为DTO对象
        InsurancePolicyResDTO result = BeanUtil.toBean(insurancePolicy, InsurancePolicyResDTO.class);

        // 获取与保险单关联的客户ID列表
        List<Long> CustomerIdList = customerInfoMapper.getCustomerInfoByPolicyId(id);

        // 初始化客户信息列表和保险产品列表
        List<CustomerInfo> customerInfoList = new ArrayList<>();
        List<InsuranceProduct> insuranceProductList = new ArrayList<>();

        // 遍历客户ID列表，获取每个客户的详细信息
        for (Long i : CustomerIdList) {
            CustomerInfo customerInfo = customerInfoMapper.getCustomerInfoById(i);
            customerInfoList.add(customerInfo);
        }

        // 将客户信息列表和保险产品列表设置到结果DTO中
        result.setCustomerInfoList(customerInfoList);
        result.setInsuranceProductList(insuranceProductList);

        // 返回包含保险单详细信息的DTO对象
        return result;
    }


    /**
     * 根据ID更新保险策略
     * 此方法首先将请求DTO转换为保险策略对象，然后更新数据库中的对应记录
     * 接着，它删除与该策略关联的客户和产品信息，以便重新插入更新后的关联信息
     * 最后，它遍历新的客户和产品列表，将它们重新关联到更新后的保险策略
     * @param id 保险策略的唯一标识符
     * @param insurancePolicyReqDTO 包含更新后的保险策略信息的请求DTO
     */
    @Override
    public void updateInsurancePolicyById(Long id, InsurancePolicyReqDTO insurancePolicyReqDTO) {

        // 将请求DTO转换为保险策略对象
        InsurancePolicy insurancePolicy = BeanUtil.toBean(insurancePolicyReqDTO, InsurancePolicy.class);

        // 更新数据库中的保险策略记录
        //insurancePolicyMapper.updateInsurancePolicyById(id, insurancePolicy);
    }


    /**
     * 根据保单ID删除保单信息
     * 此方法首先检查保单是否存在，如果存在，则删除与该保单相关的所有信息，
     * 包括保单本身、与客户的关联、以及与产品的关联如果保单不存在，则抛出运行时异常
     * @param id 保单的唯一标识符
     * @throws RuntimeException 如果尝试删除不存在的保单信息时抛出
     */
    @Override
    public void deleteInsurancePolicyById(Long id) {

        // 检查保单是否存在
        int count = insurancePolicyMapper.getInsurancePolicyCount(id);
        if(count > 0){
            // 保单存在，执行删除操作
            insurancePolicyMapper.deleteInsurancePolicyById(id);
            customerInfoMapper.deleteInsurancePolicyByPolicyId(id);
        }
        // 保单不存在，抛出异常
        throw new RuntimeException("您要删除的保单信息不存在");
    }

    @Override
    public PageBean getPoliciesByCondition(PolicyQueryRequest policyQueryRequest) {
        PageHelper.startPage(policyQueryRequest.getPageNum(), policyQueryRequest.getPageSize());
        Page resultPage = insurancePolicyMapper.list(policyQueryRequest);

        return new PageBean(resultPage.getTotal(), resultPage.getResult());
    }
}
