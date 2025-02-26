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

@Service
public class InsurancePolicyServiceImpl implements InsurancePolicyService {

    private static final String ADMIN_USER = "admin";
    private static final String POLICYHOLDER_TYPE = "A";
    private static final String INSURED_TYPE = "B";
    private static final String BENEFICIARY_TYPE = "S";
    private static final String BENEFICIARYTYPE_STATUTORY = "statutory";
    private static final String BENEFICIARYTYPE_DESIGNATED = "designated";

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

    private final SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(1);

    /**
     * 插入新的保单
     * @param insurancePolicyReqDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertInsurancePolicy(InsurancePolicyReqDTO insurancePolicyReqDTO) {
        // 生成唯一的保单号和投保单号
        long policyNo = snowflakeIdWorker.nextId();
        long proposalNo = snowflakeIdWorker.nextId();

        // 检查保险公司信息是否合规
        checkInsuranceCompany(insurancePolicyReqDTO);
        // 检查保险产品信息是否合规
        checkInsuranceProduct(insurancePolicyReqDTO);
        // 检查保单状态是否合规
        checkPolicyStatus(insurancePolicyReqDTO);

        // 检查并获取受益人类型
        String beneficiaryType = checkBeneficiaryType(insurancePolicyReqDTO);
        // 获取客户信息列表
        List<CustomerInfo> customerInfoList = insurancePolicyReqDTO.getCustomerInfoList();
        // 检查客户证件类型是否合规
        checkCustomerInfoIdType(customerInfoList);

        // 验证并插入客户信息
        validateAndInsertCustomerInfo(customerInfoList, policyNo, beneficiaryType);

        // 获取产品ID列表
        List<Long> productIdList = new ArrayList<>();
        for (UserInputProduct userInputProduct : insurancePolicyReqDTO.getProductList()) {
            productIdList.add(userInputProduct.getId());
        }

        // 根据产品ID列表查询产品信息
        List<InsuranceProduct> productList = insuranceProductMapper.selectInsuranceProductByIdList(productIdList);
        // 创建保险单列表
        List<InsurancePolicy> newPolicyList = createInsurancePolicies(insurancePolicyReqDTO, productList, policyNo, proposalNo);

        // 批量插入保险单信息
        insurancePolicyMapper.batchInsertInsurancePolicy(newPolicyList);
    }

    /**
     * 根据ID查询保险单信息
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public InsurancePolicyResDTO getInsurancePolicyById(Long id) {
        // 根据ID查询保险单信息
        InsurancePolicy insurancePolicy = insurancePolicyMapper.getInsurancePolicyById(id);
        // 将保险单信息转换为DTO对象
        InsurancePolicyResDTO result = BeanUtil.toBean(insurancePolicy, InsurancePolicyResDTO.class);

        // 设置保单状态和受益人类型的枚举值
        result.setPolicyStatus(enumValue.getEnumByCode(result.getPolicyStatus()));
        result.setBeneficiaryType(enumValue.getEnumByCode(result.getBeneficiaryType()));

        // 获取与保险单关联的客户信息列表
        List<CustomerInfo> customerInfoList = customerInfoMapper.getCustomerInfoByPolicyNo(insurancePolicy.getPolicyNo());
        // 设置客户证件类型的枚举值
        for (CustomerInfo customer : customerInfoList) {
            customer.setIdType(enumValue.getEnumByCode(customer.getIdType()));
        }
        result.setCustomerInfoList(customerInfoList);

        // 返回包含保险单详细信息的DTO对象
        return result;
    }

    /**
     * 根据id更新保单信息
     * @param id
     * @param insurancePolicyReqDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInsurancePolicyById(Long id, InsurancePolicyReqDTO insurancePolicyReqDTO) {
        // 根据ID查询保险单信息
        InsurancePolicy insurancePolicy = insurancePolicyMapper.getInsurancePolicyById(id);
        Long policyNo = insurancePolicy.getPolicyNo();
        Long proposalNo = insurancePolicy.getProposalNo();

        // 删除旧的保险单信息和客户信息
        insurancePolicyMapper.deleteInsurancePolicyByPolicyNo(policyNo);
        customerInfoMapper.deleteInsurancePolicyByPolicyNo(policyNo);

        // 检查保险公司信息是否合规
        checkInsuranceCompany(insurancePolicyReqDTO);
        // 检查保险产品信息是否合规
        checkInsuranceProduct(insurancePolicyReqDTO);
        // 检查保单状态是否合规
        checkPolicyStatus(insurancePolicyReqDTO);

        // 检查并获取受益人类型
        String beneficiaryType = checkBeneficiaryType(insurancePolicyReqDTO);
        // 获取客户信息列表
        List<CustomerInfo> customerInfoList = insurancePolicyReqDTO.getCustomerInfoList();
        // 检查客户证件类型是否合规
        checkCustomerInfoIdType(customerInfoList);

        // 验证并插入客户信息
        validateAndInsertCustomerInfo(customerInfoList, policyNo, beneficiaryType);

        // 获取产品ID列表
        List<Long> productIdList = new ArrayList<>();
        for (UserInputProduct userInputProduct : insurancePolicyReqDTO.getProductList()) {
            productIdList.add(userInputProduct.getId());
        }

        // 根据产品ID列表查询产品信息
        List<InsuranceProduct> productList = insuranceProductMapper.selectInsuranceProductByIdList(productIdList);
        // 创建保险单列表
        List<InsurancePolicy> newPolicyList = createInsurancePolicies(insurancePolicyReqDTO, productList, policyNo, proposalNo);

        // 批量插入保险单信息
        insurancePolicyMapper.batchInsertInsurancePolicy(newPolicyList);
    }

    /**
     * 根据ID删除保险单信息
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInsurancePolicyById(Long id) {
        // 根据ID查询保险单信息
        InsurancePolicy insurancePolicy = insurancePolicyMapper.getInsurancePolicyById(id);
        if (BeanUtil.isNotEmpty(insurancePolicy)) {
            // 删除保险单信息和关联的客户信息
            insurancePolicyMapper.deleteInsurancePolicy(insurancePolicy.getPolicyNo());
            customerInfoMapper.deleteCustomerInfoByPolicyNo(insurancePolicy.getPolicyNo());
        } else {
            // 如果保险单不存在，抛出异常
            throw new RuntimeException("您要删除的保单信息不存在");
        }
    }

    /**
     * 保单分页查询
     * @param policyQueryRequest
     * @return
     */
    @Override
    public PageBean getPoliciesByCondition(PolicyQueryRequest policyQueryRequest) {
        // 使用PageHelper进行分页查询
        PageHelper.startPage(policyQueryRequest.getPageNum(), policyQueryRequest.getPageSize());
        // 执行查询
        Page resultPage = insurancePolicyMapper.list(policyQueryRequest);
        // 返回分页结果
        return new PageBean(resultPage.getTotal(), resultPage.getResult());
    }

    /**
     * 检查保险公司信息是否合规
     *
     * @param insurancePolicyReqDTO 保险单请求数据传输对象
     * @throws RuntimeException 如果保险公司信息不合法
     */
    private void checkInsuranceCompany(InsurancePolicyReqDTO insurancePolicyReqDTO) {
        int companyCount = insuranceCompanyMapper.selectCompanyByCodeAndName(insurancePolicyReqDTO.getCompanyCode(), insurancePolicyReqDTO.getCompanyName());
        if (companyCount == 0) {
            throw new RuntimeException("请输入合规的保司代码及名称");
        }
    }

    /**
     * 检查客户证件类型是否合规
     *
     * @param customerInfoList 客户信息列表
     * @throws RuntimeException 如果客户证件类型不合法
     */
    private void checkCustomerInfoIdType(List<CustomerInfo> customerInfoList) {
        List<String> enumCodeList = new ArrayList<>();
        for (CustomerInfo customer : customerInfoList) {
            enumCodeList.add(customer.getIdType());
        }

        List<String> enumNameList = enumValue.getEnumNameByEnumCodeList(enumCodeList);
        for (String customerInfoIdType : enumNameList) {
            if (BeanUtil.isEmpty(customerInfoIdType)) {
                throw new RuntimeException("请输入合规的证件类型");
            }
        }
    }

    /**
     * 检查保险产品信息是否合规
     *
     * @param insurancePolicyReqDTO 保险单请求数据传输对象
     * @throws RuntimeException 如果保险产品信息不合法
     */
    private void checkInsuranceProduct(InsurancePolicyReqDTO insurancePolicyReqDTO) {
        List<InsuranceProduct> userProductList = new ArrayList<>();
        for (UserInputProduct userInputProduct : insurancePolicyReqDTO.getProductList()) {
            InsuranceProduct insuranceProduct = BeanUtil.toBean(userInputProduct, InsuranceProduct.class);
            userProductList.add(insuranceProduct);
        }

        int productCount = insuranceProductMapper.getInsuranceProductByCodeAndYearsAndMethod(userProductList);
        if (userProductList.size() != productCount) {
            throw new RuntimeException("请检查您输入的险种信息");
        }
    }

    /**
     * 检查保单状态是否合规
     *
     * @param insurancePolicyReqDTO 保险单请求数据传输对象
     * @throws RuntimeException 如果保单状态不合法
     */
    private void checkPolicyStatus(InsurancePolicyReqDTO insurancePolicyReqDTO) {
        String policyStatus = enumValue.getEnumByCode(insurancePolicyReqDTO.getPolicyStatus());
        if (BeanUtil.isEmpty(policyStatus)) {
            throw new RuntimeException("请输入合规的保单状态");
        }
    }

    /**
     * 检查受益人类型是否合规
     *
     * @param insurancePolicyReqDTO 保险单请求数据传输对象
     * @return 受益人类型
     * @throws RuntimeException 如果受益人类型不合法
     */
    private String checkBeneficiaryType(InsurancePolicyReqDTO insurancePolicyReqDTO) {
        String beneficiaryType = enumValue.getEnumByCode(insurancePolicyReqDTO.getBeneficiaryType());
        if (BeanUtil.isEmpty(beneficiaryType)) {
            throw new RuntimeException("输入的受益人类型不合法");
        }
        return insurancePolicyReqDTO.getBeneficiaryType();
    }

    /**
     * 验证并插入客户信息
     *
     * @param customerInfoList 客户信息列表
     * @param policyNo         保单号
     * @param beneficiaryType  受益人类型
     * @throws RuntimeException 如果客户信息不合法
     */
    private void validateAndInsertCustomerInfo(List<CustomerInfo> customerInfoList, long policyNo, String beneficiaryType) {
        long policyholderCount = 0;
        long insuredCount = 0;
        long beneficiaryCount = 0;

        // 统计投保人、被保人和受益人的数量
        for (CustomerInfo customer : customerInfoList) {
            if (POLICYHOLDER_TYPE.equals(customer.getCustomerType())) {
                policyholderCount++;
            } else if (INSURED_TYPE.equals(customer.getCustomerType())) {
                insuredCount++;
            } else if (BENEFICIARY_TYPE.equals(customer.getCustomerType())) {
                beneficiaryCount++;
            }
        }

        // 根据受益人类型验证客户信息
        if (BENEFICIARYTYPE_STATUTORY.equals(beneficiaryType)) {
            if (policyholderCount == 0 || insuredCount == 0 || beneficiaryCount != 0) {
                throw new RuntimeException("受益人状态为法定的情况用户不可指定受益人");
            }
        } else if (BENEFICIARYTYPE_DESIGNATED.equals(beneficiaryType)) {
            if (policyholderCount == 0 || insuredCount == 0 || beneficiaryCount == 0) {
                throw new RuntimeException("请检查客户信息是否正确");
            }
            int beneficiaryRate = 0;
            for (CustomerInfo customer : customerInfoList) {
                if (BENEFICIARY_TYPE.equals(customer.getCustomerType())) {
                    beneficiaryRate += customer.getBenefitRatio();
                }
            }
            if (beneficiaryRate != 100) {
                throw new RuntimeException("请检查收益人收益比例是否正确");
            }
        } else {
            throw new RuntimeException("您输入的受益人类型不正确,请重新检查");
        }

        // 插入客户信息
        List<CustomerInfo> newCustomerInfoList = new ArrayList<>();
        for (CustomerInfo customer : customerInfoList) {
            customer.setId(snowflakeIdWorker.nextId());
            customer.setPolicyNo(policyNo);
            customer.setCreator(ADMIN_USER);
            customer.setCreatedTime(LocalDateTime.now());
            customer.setUpdater(ADMIN_USER);
            customer.setUpdatedTime(LocalDateTime.now());
            customer.setIsDeleted(0);
            newCustomerInfoList.add(customer);
        }

        customerInfoMapper.batchInsertCustomerInfo(newCustomerInfoList);
    }

    /**
     * 创建保险单列表
     *
     * @param insurancePolicyReqDTO 保险单请求数据传输对象
     * @param productList           保险产品列表
     * @param policyNo              保单号
     * @param proposalNo            投保单号
     * @return 保险单列表
     */
    private List<InsurancePolicy> createInsurancePolicies(InsurancePolicyReqDTO insurancePolicyReqDTO, List<InsuranceProduct> productList, long policyNo, long proposalNo) {
        List<InsurancePolicy> newPolicyList = new ArrayList<>();
        for (InsuranceProduct product : productList) {
            InsurancePolicy insurancePolicy = BeanUtil.toBean(insurancePolicyReqDTO, InsurancePolicy.class);
            insurancePolicy.setId(snowflakeIdWorker.nextId());
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
            newPolicyList.add(insurancePolicy);
        }
        return newPolicyList;
    }
}