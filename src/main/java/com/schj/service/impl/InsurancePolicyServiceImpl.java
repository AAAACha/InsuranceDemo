package com.schj.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.schj.mapper.*;
import com.schj.pojo.dto.request.InsurancePolicyReqDTO;
import com.schj.pojo.dto.request.PolicyQueryRequest;
import com.schj.pojo.dto.response.InsurancePolicyResDTO;
import com.schj.pojo.po.*;
import com.schj.service.InsurancePolicyService;
import com.schj.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private InsurancePolicyMapper insurancePolicyMapper;

    @Autowired
    private CustomerInfoMapper customerInfoMapper;

    @Autowired
    private InsuranceProductMapper insuranceProductMapper;

    @Autowired
    private ProductPolicyMapper productPolicyMapper;

    @Autowired
    private EnumValue enumValue;

    // 创建Snowflake ID生成器实例
    SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(1);

    @Override
    @Transactional
    /**
     * 插入保险单信息及其关联信息
     *
     * @param insurancePolicyReqDTO 保险单请求数据传输对象，包含保险单基本信息、产品ID列表和客户信息列表
     * 此方法首先生成一个唯一的保险单ID，然后将请求数据转换为保险单实体对象，
     * 验证受益人类型并设置相关属性，最后将保险单信息、产品策略关联信息和客户信息插入数据库
     * 如果受益人类型不合法或客户信息不符合规定条件，将抛出运行时异常
     */
    public void insertInsurancePolicy(InsurancePolicyReqDTO insurancePolicyReqDTO) {

        // 生成唯一的保险单ID
        long id = snowflakeIdWorker.nextId();

        // 将请求数据转换为保险单实体对象
        InsurancePolicy insurancePolicy = BeanUtil.toBean(insurancePolicyReqDTO, InsurancePolicy.class);

        // 获取并验证受益人类型
        String beneficiaryType = enumValue.getEnumByCode(insurancePolicyReqDTO.getBeneficiaryType());
        if (BeanUtil.isNotEmpty(beneficiaryType)) {
            insurancePolicyReqDTO.setBeneficiaryType(beneficiaryType);
        } else {
            // 如果受益人类型不合法，抛出异常
            throw new RuntimeException(" 输入的受益人类型不合法");
        }

        // 设置保险单的基本信息
        insurancePolicy.setId(id);
        insurancePolicy.setCreator("admin");
        insurancePolicy.setCreatedTime(LocalDateTime.now());
        insurancePolicy.setUpdater("admin");
        insurancePolicy.setUpdatedTime(LocalDateTime.now());
        insurancePolicy.setIsDeleted(0);

        // 插入保险单信息到数据库
        insurancePolicyMapper.insertInsurancePolicy(insurancePolicy);

        // 获取产品ID列表
        List<Long> productIdList = insurancePolicyReqDTO.getProductIdList();

        // 遍历产品ID列表，创建并插入产品策略关联信息
        for (Long l : productIdList) {
            ProductPolicy productPolicy = new ProductPolicy();
            long productPolicyId = snowflakeIdWorker.nextId();
            productPolicy.setId(productPolicyId);
            productPolicy.setPolicyId(id);
            productPolicy.setProductId(l);

            // 插入产品策略关联信息到数据库
            productPolicyMapper.insertProductPolicy(productPolicy);
        }

        // 获得客户列表
        List<CustomerInfo> customerInfoList = insurancePolicyReqDTO.getCustomerInfoList();

        // 投保人个数
        long policyholdercount = customerInfoList.stream().filter(customer -> "A".equals(customer.getCustomerType())).count();
        // 被保人个数
        long insuredcount = customerInfoList.stream().filter(customer -> "B".equals(customer.getCustomerType())).count();
        // 受益人个数
        long Beneficiarycount = customerInfoList.stream().filter(customer -> "S".equals(customer.getCustomerType())).count();

        // 根据受益人类型处理客户信息
        beneficiaryType = insurancePolicyReqDTO.getBeneficiaryType();
        if ("法定".equals(beneficiaryType)) {
            // 如果受益人类型为法定，验证投保人和被保人存在且受益人不存在
            if (policyholdercount > 0 && insuredcount > 0 && Beneficiarycount == 0) {
                // 插入所有客户信息到数据库
                for (CustomerInfo customerInfo : customerInfoList) {
                    customerInfo.setCreator("admin");
                    customerInfo.setCreatedTime(LocalDateTime.now());
                    customerInfo.setUpdater("admin");
                    customerInfo.setUpdatedTime(LocalDateTime.now());
                    customerInfo.setIsDeleted(0);
                    customerInfoMapper.insertCustomerInfoService(customerInfo);
                }
            } else {
                // 如果不符合条件，抛出异常
                throw new RuntimeException("受益人状态时法定的情况用户不可指定受益人");
            }
        } else if ("指定".equals(beneficiaryType)) {
            // 如果受益人类型为指定，验证投保人、被保人和受益人均存在
            if (policyholdercount > 0 && insuredcount > 0 && Beneficiarycount > 0) {
                int beneficiaryRate = 0;

                // 计算所有受益人的受益比例总和
                for (CustomerInfo customerInfo : customerInfoList) {
                    if ("S".equals(customerInfo.getCustomerType())) {
                        beneficiaryRate += customerInfo.getBenefitRatio();
                    }
                }

                // 如果受益比例总和为100%，插入所有客户信息到数据库
                if (beneficiaryRate == 100) {
                    for (CustomerInfo customerInfo : customerInfoList) {
                        customerInfo.setCreator("admin");
                        customerInfo.setCreatedTime(LocalDateTime.now());
                        customerInfo.setUpdater("admin");
                        customerInfo.setUpdatedTime(LocalDateTime.now());
                        customerInfo.setIsDeleted(0);
                        customerInfoMapper.insertCustomerInfoService(customerInfo);
                    }
                }
            } else {
                // 如果不符合条件，抛出异常
                throw new RuntimeException("请检查投保人信息是否正确");
            }
        }
    }


    /**
     * 根据保险单ID获取保险单详情
     * 此方法首先从数据库中检索指定ID的保险单信息，然后获取与该保险单关联的客户和产品信息，
     * 最后将这些信息封装到一个DTO中返回这主要用于展示保险单的详细信息，包括投保客户和包含的产品
     *
     * @param id 保险单的唯一标识符
     * @return 包含保险单详细信息的DTO对象
     */
    @Override
    @Transactional
    public InsurancePolicyResDTO getInsurancePolicyById(Long id) {
        // 根据ID从数据库中获取保险单信息
        InsurancePolicy insurancePolicy = insurancePolicyMapper.getInsurancePolicyById(id);

        // 将保险单实体转换为DTO对象
        InsurancePolicyResDTO result = BeanUtil.toBean(insurancePolicy, InsurancePolicyResDTO.class);

        // 获取与保险单关联的客户ID列表
        List<CustomerInfo> customerInfoList = customerInfoMapper.getCustomerInfoByPolicyId(id);

        // 获取与保险单关联的险种ID列表
        List<Long> insuranceProductIdList = productPolicyMapper.getProductIdListByPolicyId(id);

        // 初始化客户信息列表和险种列表
        List<InsuranceProduct> insuranceProductList = new ArrayList<>();

        // 遍历险种ID列表，获取每个险种的详细信息
        for (Long i : insuranceProductIdList) {
            InsuranceProduct insuranceProduct = insuranceProductMapper.getInsuranceProductById(i);
            insuranceProductList.add(insuranceProduct);
        }

        // 将客户信息列表和险种列表设置到结果DTO中
        result.setCustomerInfoList(customerInfoList);
        result.setInsuranceProductList(insuranceProductList);

        // 返回包含保险单详细信息的DTO对象
        return result;
    }

    @Override
    @Transactional
    public void updateInsurancePolicyById(Long id, InsurancePolicyReqDTO insurancePolicyReqDTO) {

        InsurancePolicy insurancePolicy = BeanUtil.toBean(insurancePolicyReqDTO, InsurancePolicy.class);

        // 获取并验证受益人类型
        String beneficiaryType = enumValue.getEnumByCode(insurancePolicyReqDTO.getBeneficiaryType());
        if (BeanUtil.isNotEmpty(beneficiaryType)) {
            insurancePolicyReqDTO.setBeneficiaryType(beneficiaryType);
        } else {
            // 如果受益人类型不合法，抛出异常
            throw new RuntimeException(" 输入的受益人类型不合法");
        }

        insurancePolicyMapper.updateInsurancePolicyById(insurancePolicy);

        // 获得客户列表
        List<CustomerInfo> customerInfoList = insurancePolicyReqDTO.getCustomerInfoList();

        // 投保人个数
        long policyholdercount = customerInfoList.stream().filter(customer -> "A".equals(customer.getCustomerType())).count();
        // 被保人个数
        long insuredcount = customerInfoList.stream().filter(customer -> "B".equals(customer.getCustomerType())).count();
        // 受益人个数
        long Beneficiarycount = customerInfoList.stream().filter(customer -> "S".equals(customer.getCustomerType())).count();

        // 根据受益人类型处理客户信息
        beneficiaryType = insurancePolicyReqDTO.getBeneficiaryType();
        if ("法定".equals(beneficiaryType)) {
            // 如果受益人类型为法定，验证投保人和被保人存在且受益人不存在
            if (policyholdercount > 0 && insuredcount > 0 && Beneficiarycount == 0) {
                // 插入所有客户信息到数据库
                for (CustomerInfo customerInfo : customerInfoList) {
                    customerInfo.setCreator("admin");
                    customerInfo.setCreatedTime(LocalDateTime.now());
                    customerInfo.setUpdater("admin");
                    customerInfo.setUpdatedTime(LocalDateTime.now());
                    customerInfo.setIsDeleted(0);
                    customerInfoMapper.insertCustomerInfoService(customerInfo);
                }
            } else {
                // 如果不符合条件，抛出异常
                throw new RuntimeException("受益人状态时法定的情况用户不可指定受益人");
            }
        } else if ("指定".equals(beneficiaryType)) {
            // 如果受益人类型为指定，验证投保人、被保人和受益人均存在
            if (policyholdercount > 0 && insuredcount > 0 && Beneficiarycount > 0) {
                int beneficiaryRate = 0;

                // 计算所有受益人的受益比例总和
                for (CustomerInfo customerInfo : customerInfoList) {
                    if ("S".equals(customerInfo.getCustomerType())) {
                        beneficiaryRate += customerInfo.getBenefitRatio();
                    }
                }

                // 如果受益比例总和为100%，插入所有客户信息到数据库
                if (beneficiaryRate == 100) {
                    for (CustomerInfo customerInfo : customerInfoList) {
                        customerInfo.setCreator("admin");
                        customerInfo.setCreatedTime(LocalDateTime.now());
                        customerInfo.setUpdater("admin");
                        customerInfo.setUpdatedTime(LocalDateTime.now());
                        customerInfo.setIsDeleted(0);
                        customerInfoMapper.insertCustomerInfoService(customerInfo);
                    }
                }
            } else {
                // 如果不符合条件，抛出异常
                throw new RuntimeException("请检查投保人信息是否正确");
            }
        }

        productPolicyMapper.deleteProductPolicyByPolicyId(id);

        // 获取产品ID列表
        List<Long> productIdList = insurancePolicyReqDTO.getProductIdList();

        // 遍历产品ID列表，创建并插入产品策略关联信息
        for (Long l : productIdList) {
            ProductPolicy productPolicy = new ProductPolicy();
            long productPolicyId = snowflakeIdWorker.nextId();
            productPolicy.setId(productPolicyId);
            productPolicy.setPolicyId(id);
            productPolicy.setProductId(l);

            // 插入产品策略关联信息到数据库
            productPolicyMapper.insertProductPolicy(productPolicy);
        }
    }


    /**
     * 根据保单ID删除保单信息
     * 此方法首先检查保单是否存在，如果存在，则删除与该保单相关的所有信息，
     * 包括保单本身、与客户的关联、以及与产品的关联如果保单不存在，则抛出运行时异常
     *
     * @param id 保单的唯一标识符
     * @throws RuntimeException 如果尝试删除不存在的保单信息时抛出
     */
    @Override
    @Transactional
    public void deleteInsurancePolicyById(Long id) {

        // 检查保单是否存在
        int count = insurancePolicyMapper.getInsurancePolicyCount(id);
        if (count > 0) {
            // 保单存在，执行删除操作
            insurancePolicyMapper.deleteInsurancePolicyById(id);
            customerInfoMapper.deleteInsurancePolicyByPolicyId(id);
            productPolicyMapper.deleteProductPolicyByPolicyId(id);
        } else {
            // 保单不存在，抛出异常
            throw new RuntimeException("您要删除的保单信息不存在");
        }
    }

    @Override
    public PageBean getPoliciesByCondition(PolicyQueryRequest policyQueryRequest) {
        PageHelper.startPage(policyQueryRequest.getPageNum(), policyQueryRequest.getPageSize());
        Page resultPage = insurancePolicyMapper.list(policyQueryRequest);

        return new PageBean(resultPage.getTotal(), resultPage.getResult());
    }
}
