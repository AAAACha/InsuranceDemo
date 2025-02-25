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
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Descriptioin insurancePolicyServiceImpl
 * @Author AvA
 * @Date 2025-02-22
 */
@Service
public class InsurancePolicyServiceImpl implements InsurancePolicyService {

    private static final String ADMIN_USER = "admin";
    private static final String POLICYHOLDER_TYPE = "A";
    private static final String INSURED_TYPE = "B";
    private static final String BENEFICIARY_TYPE = "S";

    @Autowired
    private InsurancePolicyMapper insurancePolicyMapper;

    @Autowired
    private CustomerInfoMapper customerInfoMapper;

    @Autowired
    private InsuranceProductMapper insuranceProductMapper;

    @Autowired
    private InsuranceCompanyMapper insuranceCompanyMapper;

    @Autowired
    private EnumValue enumValue;

    // 创建Snowflake ID生成器实例
    SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(1);

    @Override
    @Transactional(rollbackFor = Exception.class)
    /**
     * 插入保险单信息及其关联信息
     *
     * @param insurancePolicyReqDTO 保险单请求数据传输对象，包含保险单基本信息、产品ID列表和客户信息列表
     * 此方法首先生成一个唯一的保险单ID，然后将请求数据转换为保险单实体对象，
     * 验证受益人类型并设置相关属性，最后将保险单信息、产品策略关联信息和客户信息插入数据库
     * 如果受益人类型不合法或客户信息不符合规定条件，将抛出运行时异常
     */
    public void insertInsurancePolicy(InsurancePolicyReqDTO insurancePolicyReqDTO) {


        // 生成唯一的保单号
        long policyNo = snowflakeIdWorker.nextId();
        // 生成唯一的投保单号
        long proposalNo = snowflakeIdWorker.nextId();

        // 将请求数据转换为保险单实体对象
        InsurancePolicy insurancePolicy = BeanUtil.toBean(insurancePolicyReqDTO, InsurancePolicy.class);

        //查询保司输入是否正确
        checkInsuranceCompany(insurancePolicyReqDTO);

        // 获取并验证受益人类型
        String beneficiaryType = enumValue.getEnumByCode(insurancePolicyReqDTO.getBeneficiaryType());
        if (BeanUtil.isEmpty(beneficiaryType)) {
            // 如果受益人类型不合法，抛出异常
            throw new RuntimeException(" 输入的受益人类型不合法");
        }

        String policyStatus = enumValue.getEnumByCode(insurancePolicyReqDTO.getPolicyStatus());
        if (BeanUtil.isEmpty(policyStatus)) {
            throw new RuntimeException("请输入合规的保单状态");
        }

        // 获得客户列表
        List<CustomerInfo> customerInfoList = insurancePolicyReqDTO.getCustomerInfoList();

        //判断证件类型是否合规
        for (CustomerInfo customerInfo : customerInfoList) {
            String IdType = enumValue.getEnumByCode(customerInfo.getIdType());
            if (BeanUtil.isEmpty(IdType)) {
                throw new RuntimeException("请输入合规的证件类型");
            }
        }

        // 投保人个数
        long policyholdercount = customerInfoList.stream().filter(customer -> POLICYHOLDER_TYPE.equals(customer.getCustomerType())).count();
        // 被保人个数
        long insuredcount = customerInfoList.stream().filter(customer -> INSURED_TYPE.equals(customer.getCustomerType())).count();
        // 受益人个数
        long Beneficiarycount = customerInfoList.stream().filter(customer -> BENEFICIARY_TYPE.equals(customer.getCustomerType())).count();

        // 根据受益人类型处理客户信息
        beneficiaryType = insurancePolicyReqDTO.getBeneficiaryType();
        if ("statutory".equals(beneficiaryType)) {
            // 如果受益人类型为法定，验证投保人和被保人存在且受益人不存在
            if (policyholdercount > 0 && insuredcount > 0 && Beneficiarycount == 0) {
                // 插入所有客户信息到数据库
                for (CustomerInfo customerInfo : customerInfoList) {
                    customerInfo.setCreator(ADMIN_USER);
                    customerInfo.setCreatedTime(LocalDateTime.now());
                    customerInfo.setUpdater(ADMIN_USER);
                    customerInfo.setUpdatedTime(LocalDateTime.now());
                    customerInfo.setIsDeleted(0);
                    customerInfoMapper.insertCustomerInfoService(customerInfo);
                }
            } else {
                // 如果不符合条件，抛出异常
                throw new RuntimeException("受益人状态时法定的情况用户不可指定受益人");
            }
        } else if ("designated".equals(beneficiaryType)) {
            // 如果受益人类型为指定，验证投保人、被保人和受益人均存在
            if (policyholdercount > 0 && insuredcount > 0 && Beneficiarycount > 0) {
                int beneficiaryRate = 0;

                // 计算所有受益人的受益比例总和
                for (CustomerInfo customerInfo : customerInfoList) {
                    if (BENEFICIARY_TYPE.equals(customerInfo.getCustomerType())) {
                        beneficiaryRate += customerInfo.getBenefitRatio();
                    }
                }

                // 如果受益比例总和为100%，插入所有客户信息到数据库
                if (beneficiaryRate == 100) {
                    for (CustomerInfo customerInfo : customerInfoList) {
                        customerInfo.setPolicyNo(policyNo);
                        customerInfo.setCreator(ADMIN_USER);
                        customerInfo.setCreatedTime(LocalDateTime.now());
                        customerInfo.setUpdater(ADMIN_USER);
                        customerInfo.setUpdatedTime(LocalDateTime.now());
                        customerInfo.setIsDeleted(0);
                        customerInfoMapper.insertCustomerInfoService(customerInfo);
                    }
                } else {
                    throw new RuntimeException("请检查投保人信息是否正确");
                }
            } else {
                // 如果不符合条件，抛出异常
                throw new RuntimeException("请检查投保人信息是否正确");
            }
        }

        // 获取产品ID列表
        List<Long> productIdList = insurancePolicyReqDTO.getProductIdList();

        for (Long l : productIdList) {

            InsuranceProduct product = insuranceProductMapper.getInsuranceProductById(l);
            if (BeanUtil.isEmpty(product)) {
                throw new RuntimeException("请输入合规的险种ID");
            }

            if ("approval".equals(product.getProductStatus())) {
                throw new RuntimeException("请输入有效状态合规的险种ID");
            }

            if ("out_of_sale".equals(product.getProductStatus())) {
                throw new RuntimeException("请输入有效状态合规的险种ID");
            }
            // 生成唯一的保险单主键
            long id = snowflakeIdWorker.nextId();
            // 设置保险单的基本信息
            insurancePolicy.setId(id);
            insurancePolicy.setPolicyNo(policyNo);
            insurancePolicy.setProposalNo(proposalNo);
            insurancePolicy.setProductCode(product.getProductCode());
            insurancePolicy.setProductName(product.getProductName());
            insurancePolicy.setPaymentYears(product.getPaymentYears());
            insurancePolicy.setPaymentMethod(product.getPaymentMethod());
            insurancePolicy.setCreator(ADMIN_USER);
            insurancePolicy.setCreatedTime(LocalDateTime.now());
            insurancePolicy.setUpdater(ADMIN_USER);
            insurancePolicy.setUpdatedTime(LocalDateTime.now());
            insurancePolicy.setIsDeleted(0);

            // 插入保险单信息到数据库
            insurancePolicyMapper.insertInsurancePolicy(insurancePolicy);
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
    @Transactional(rollbackFor = Exception.class)
    public InsurancePolicyResDTO getInsurancePolicyById(Long id) {
        // 根据ID从数据库中获取保险单信息
        InsurancePolicy insurancePolicy = insurancePolicyMapper.getInsurancePolicyById(id);

        // 将保险单实体转换为DTO对象
        InsurancePolicyResDTO result = BeanUtil.toBean(insurancePolicy, InsurancePolicyResDTO.class);

        String policyStatus = enumValue.getEnumByCode(result.getPolicyStatus());
        if(BeanUtil.isNotEmpty(policyStatus)){
            result.setPolicyStatus(policyStatus);
        } else {
            throw new RuntimeException("您查询的保单的状态不合法");
        }

        String beneficiaryType = enumValue.getEnumByCode(result.getBeneficiaryType());
        if(BeanUtil.isNotEmpty(beneficiaryType)){
            result.setBeneficiaryType(beneficiaryType);
        } else {
            throw new RuntimeException("您查询的保单的受益人类型不合法");
        }

        String paymentMethod = enumValue.getEnumByCode(result.getPaymentMethod());
        if(BeanUtil.isNotEmpty(paymentMethod)){
            result.setPaymentMethod(paymentMethod);
        } else {
            throw new RuntimeException("您查询的保单的缴费方式类型不合法");
        }

        // 获取与保险单关联的客户ID列表
        List<CustomerInfo> customerInfoList = customerInfoMapper.getCustomerInfoByPolicyNo(insurancePolicy.getPolicyNo());

        List<CustomerInfo> customerInfo = customerInfoList.stream().peek(customer -> customer.setIdType(enumValue.getEnumByCode(customer.getIdType()))).collect(Collectors.toList());

        // 将客户信息列表和险种列表设置到结果DTO中
        result.setCustomerInfoList(customerInfo);

        // 返回包含保险单详细信息的DTO对象
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInsurancePolicyById(Long id, InsurancePolicyReqDTO insurancePolicyReqDTO) {

        InsurancePolicy insurancePolicy = insurancePolicyMapper.getInsurancePolicyById(id);
        Long policyNo = insurancePolicy.getPolicyNo();
        Long proposalNo = insurancePolicy.getProposalNo();
        //删除旧的保单信息
        insurancePolicyMapper.deleteInsurancePolicyByPolicyNo(policyNo);
        //删除旧的客户信息
        customerInfoMapper.deleteInsurancePolicyByPolicyNo(policyNo);

        insurancePolicy = BeanUtil.toBean(insurancePolicyReqDTO, InsurancePolicy.class);

        // 获取并验证受益人类型
        String beneficiaryType = enumValue.getEnumByCode(insurancePolicyReqDTO.getBeneficiaryType());
        if (BeanUtil.isEmpty(beneficiaryType)) {
            // 如果受益人类型不合法，抛出异常
            throw new RuntimeException(" 输入的受益人类型不合法");
        }

        checkInsuranceCompany(insurancePolicyReqDTO);

        //插入新的
        // 获得客户列表
        List<CustomerInfo> customerInfoList = insurancePolicyReqDTO.getCustomerInfoList();

        //判断证件类型是否合规
        for (CustomerInfo customerInfo : customerInfoList) {
            String IdType = enumValue.getEnumByCode(customerInfo.getIdType());
            if (BeanUtil.isEmpty(IdType)) {
                throw new RuntimeException("请输入合规的证件类型");
            }
        }

        // 投保人个数
        long policyholdercount = customerInfoList.stream().filter(customer -> POLICYHOLDER_TYPE.equals(customer.getCustomerType())).count();
        // 被保人个数
        long insuredcount = customerInfoList.stream().filter(customer -> INSURED_TYPE.equals(customer.getCustomerType())).count();
        // 受益人个数
        long Beneficiarycount = customerInfoList.stream().filter(customer -> BENEFICIARY_TYPE.equals(customer.getCustomerType())).count();

        // 根据受益人类型处理客户信息
        beneficiaryType = insurancePolicyReqDTO.getBeneficiaryType();
        if ("statutory".equals(beneficiaryType)) {
            // 如果受益人类型为法定，验证投保人和被保人存在且受益人不存在
            if (policyholdercount > 0 && insuredcount > 0 && Beneficiarycount == 0) {
                // 插入所有客户信息到数据库
                for (CustomerInfo customerInfo : customerInfoList) {
                    customerInfo.setPolicyNo(policyNo);
                    customerInfo.setCreator(ADMIN_USER);
                    customerInfo.setCreatedTime(LocalDateTime.now());
                    customerInfo.setUpdater(ADMIN_USER);
                    customerInfo.setUpdatedTime(LocalDateTime.now());
                    customerInfo.setIsDeleted(0);
                    customerInfoMapper.insertCustomerInfoService(customerInfo);
                }
            } else {
                // 如果不符合条件，抛出异常
                throw new RuntimeException("受益人状态时法定的情况用户不可指定受益人");
            }
        } else if ("designated".equals(beneficiaryType)) {
            // 如果受益人类型为指定，验证投保人、被保人和受益人均存在
            if (policyholdercount > 0 && insuredcount > 0 && Beneficiarycount > 0) {
                int beneficiaryRate = 0;

                // 计算所有受益人的受益比例总和
                for (CustomerInfo customerInfo : customerInfoList) {
                    if (BENEFICIARY_TYPE.equals(customerInfo.getCustomerType())) {
                        beneficiaryRate += customerInfo.getBenefitRatio();
                    }
                }

                // 如果受益比例总和为100%，插入所有客户信息到数据库
                if (beneficiaryRate == 100) {
                    for (CustomerInfo customerInfo : customerInfoList) {
                        customerInfo.setPolicyNo(policyNo);
                        customerInfo.setCreator(ADMIN_USER);
                        customerInfo.setCreatedTime(LocalDateTime.now());
                        customerInfo.setUpdater(ADMIN_USER);
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

        // 获取产品ID列表
        List<Long> productIdList = insurancePolicyReqDTO.getProductIdList();

        // 遍历产品ID列表，创建并插入产品策略关联信息
        for (Long l : productIdList) {
            long newId = snowflakeIdWorker.nextId();

            //判断insurance_product表中是否存在id为l的数据
            InsuranceProduct product = insuranceProductMapper.getInsuranceProductById(l);
            if (BeanUtil.isEmpty(product)) {
                throw new RuntimeException("请输入合规的险种ID");
            }

            if ("approval".equals(product.getProductStatus())) {
                throw new RuntimeException("请输入有效状态合规的险种ID");
            }

            if ("out_of_sale".equals(product.getProductStatus())) {
                throw new RuntimeException("请输入有效状态合规的险种ID");
            }

            insurancePolicy.setId(newId);
            insurancePolicy.setPolicyNo(policyNo);
            insurancePolicy.setProposalNo(proposalNo);
            insurancePolicy.setProductCode(product.getProductCode());
            insurancePolicy.setProductName(product.getProductName());
            insurancePolicy.setPaymentYears(product.getPaymentYears());
            insurancePolicy.setPaymentMethod(product.getPaymentMethod());
            insurancePolicy.setCreator(ADMIN_USER);
            insurancePolicy.setCreatedTime(LocalDateTime.now());
            insurancePolicy.setUpdater(ADMIN_USER);
            insurancePolicy.setUpdatedTime(LocalDateTime.now());
            insurancePolicy.setIsDeleted(0);

            // 插入保险单信息到数据库
            insurancePolicyMapper.insertInsurancePolicy(insurancePolicy);
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
    @Transactional(rollbackFor = Exception.class)
    public void deleteInsurancePolicyById(Long id) {

        // 检查保单是否存在
        InsurancePolicy insurancePolicy = insurancePolicyMapper.getInsurancePolicyById(id);
        if (BeanUtil.isNotEmpty(insurancePolicy)) {
            insurancePolicyMapper.deleteInsurancePolicy(insurancePolicy.getPolicyNo());
            customerInfoMapper.deleteCustomerInfoByPolicyNo(insurancePolicy.getPolicyNo());
        } else {
            // 保单不存在，抛出异常
            throw new RuntimeException("您要删除的保单信息不存在");
        }
    }

    /**
     * 保单信息分页查询
     *
     * @param policyQueryRequest
     * @return
     */
    @Override
    public PageBean getPoliciesByCondition(PolicyQueryRequest policyQueryRequest) {
        PageHelper.startPage(policyQueryRequest.getPageNum(), policyQueryRequest.getPageSize());
        Page resultPage = insurancePolicyMapper.list(policyQueryRequest);

        return new PageBean(resultPage.getTotal(), resultPage.getResult());
    }

    /**
     * 查询保司输入是否正确
     *
     * @param insurancePolicyReqDTO
     */
    private void checkInsuranceCompany(InsurancePolicyReqDTO insurancePolicyReqDTO) {
        int companycount = insuranceCompanyMapper.selectCompanyByCodeAndName(insurancePolicyReqDTO.getCompanyCode(), insurancePolicyReqDTO.getCompanyName());

        if (companycount == 0) {
            throw new RuntimeException("请输入合规的保司代码及名称");
        }
    }
}
