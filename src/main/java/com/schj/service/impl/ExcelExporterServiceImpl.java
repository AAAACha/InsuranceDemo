package com.schj.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcel;
import com.schj.mapper.CustomerInfoMapper;
import com.schj.mapper.EnumValue;
import com.schj.mapper.InsurancePolicyMapper;
import com.schj.pojo.po.CustomerInfo;
import com.schj.pojo.po.ExcelExporter;
import com.schj.pojo.po.InsurancePolicy;
import com.schj.pojo.po.Result;
import com.schj.service.ExcelExporterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Descriptioin ExcelExporterServiceImpl
 * @Author AvA
 * @Date 2025-02-22
 */
@Service
public class ExcelExporterServiceImpl implements ExcelExporterService {

    @Autowired
    private InsurancePolicyMapper insurancePolicyMapper;

    @Autowired
    private CustomerInfoMapper customerInfoMapper;
    @Autowired
    private EnumValue enumValue;


    @Override
    @Transactional
    public Result ExcelExporter(int count) {
        // 定义Excel文件的路径和名称，使用格式化后的日期时间字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String fileName = LocalDateTime.now().format(formatter) + "_保单信息表.xlsx";
        File excelFile = new File("D:/Test/" + fileName);

        // 检查并创建Excel文件
        createExcelFileIfNotExists(excelFile);

        // 从数据库获取指定数量的保险政策信息列表
        List<InsurancePolicy> insurancePolicyList = insurancePolicyMapper.getInsurancePolicyList(count);

        // 将保险政策信息转换为Excel导出对象列表
        List<ExcelExporter> excelExporterList = convertToExcelExporterList(insurancePolicyList);

        // 使用EasyExcel库将保险政策信息列表写入Excel文件中
        writeExcelFile(excelFile, excelExporterList);

        return Result.success();
    }

    /**
     * 创建Excel文件（如果文件不存在）
     *
     * @param excelFile Excel文件对象
     */
    private void createExcelFileIfNotExists(File excelFile) {
        if (!excelFile.exists()) {
            try {
                boolean created = excelFile.createNewFile();
                if (!created) {
                    throw new RuntimeException("无法创建Excel文件: " + excelFile.getAbsolutePath());
                }
            } catch (IOException e) {
                throw new RuntimeException("创建Excel文件失败: " + e.getMessage(), e);
            }
        }
    }

    /**
     * 将保险政策信息列表转换为Excel导出对象列表
     *
     * @param insurancePolicyList 保险政策信息列表
     * @return Excel导出对象列表
     */
    private List<ExcelExporter> convertToExcelExporterList(List<InsurancePolicy> insurancePolicyList) {
        List<ExcelExporter> excelExporterList = new ArrayList<>();

        for (InsurancePolicy insurancePolicy : insurancePolicyList) {
            ExcelExporter excelExporter = BeanUtil.toBean(insurancePolicy, ExcelExporter.class);

            // 获取与当前保险政策关联的客户信息列表
            List<CustomerInfo> customerInfoList = customerInfoMapper.getCustomerInfoByPolicyNo(insurancePolicy.getPolicyNo());

            // 设置投保人、被保险人和受益人信息
            setCustomerInfo(excelExporter, customerInfoList);

            // 设置枚举值（保单状态、受益人类型、缴费方式）
            setEnumValues(excelExporter);

            // 添加到导出列表
            excelExporterList.add(excelExporter);
        }

        return excelExporterList;
    }

    /**
     * 设置投保人、被保险人和受益人信息
     *
     * @param excelExporter     Excel导出对象
     * @param customerInfoList  客户信息列表
     */
    private void setCustomerInfo(ExcelExporter excelExporter, List<CustomerInfo> customerInfoList) {
        String policyholder = getCustomerNamesByType(customerInfoList, "A");
        String insured = getCustomerNamesByType(customerInfoList, "B");
        String beneficiary = getCustomerNamesByType(customerInfoList, "S");

        excelExporter.setPolicyholder(policyholder);
        excelExporter.setInsured(insured);
        excelExporter.setBeneficiary(beneficiary);
    }

    /**
     * 根据客户类型获取客户名称列表
     *
     * @param customerInfoList 客户信息列表
     * @param customerType     客户类型（A: 投保人, B: 被保险人, S: 受益人）
     * @return 客户名称列表（逗号分隔）
     */
    private String getCustomerNamesByType(List<CustomerInfo> customerInfoList, String customerType) {
        return customerInfoList.stream()
                .filter(customer -> customerType.equals(customer.getCustomerType()))
                .map(CustomerInfo::getFullName)
                .collect(Collectors.joining(", "));
    }

    /**
     * 设置枚举值（保单状态、受益人类型、缴费方式）
     *
     * @param excelExporter Excel导出对象
     */
    private void setEnumValues(ExcelExporter excelExporter) {
        String policyStatus = enumValue.getEnumByCode(excelExporter.getPolicyStatus());
        if (BeanUtil.isNotEmpty(policyStatus)) {
            excelExporter.setPolicyStatus(policyStatus);
        }

        String beneficiaryType = enumValue.getEnumByCode(excelExporter.getBeneficiaryType());
        if (BeanUtil.isNotEmpty(beneficiaryType)) {
            excelExporter.setBeneficiaryType(beneficiaryType);
        }

        String paymentMethod = enumValue.getEnumByCode(excelExporter.getPaymentMethod());
        if (BeanUtil.isNotEmpty(paymentMethod)) {
            excelExporter.setPaymentMethod(paymentMethod);
        }
    }

    /**
     * 将Excel导出对象列表写入Excel文件
     *
     * @param excelFile         Excel文件对象
     * @param excelExporterList Excel导出对象列表
     */
    private void writeExcelFile(File excelFile, List<ExcelExporter> excelExporterList) {
        try {
            EasyExcel.write(excelFile, ExcelExporter.class)
                    .sheet("保单信息")
                    .doWrite(excelExporterList);
        } catch (Exception e) {
            throw new RuntimeException("写入Excel文件失败: " + e.getMessage(), e);
        }
    }
}