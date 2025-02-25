package com.schj.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcel;
import com.schj.mapper.CustomerInfoMapper;
import com.schj.mapper.EnumValue;
import com.schj.mapper.InsurancePolicyMapper;
import com.schj.pojo.po.CustomerInfo;
import com.schj.pojo.po.ExcelExporter;
import com.schj.pojo.po.InsurancePolicy;
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


    /**
     * 该方法用于导出保险政策信息到Excel文件
     * 它首先创建一个唯一的Excel文件名，然后尝试创建该文件
     * 接着，它从数据库获取指定数量的保险政策信息，遍历这些信息，并将其转换为适合导出的格式
     * 最后，使用EasyExcel库将处理后的数据写入Excel文件中
     *
     * @param count 指定从数据库中获取的保险政策信息的数量
     */
    @Override
    @Transactional
    public void ExcelExporter(int count) {
        // 定义Excel文件的路径和名称，使用格式化后的日期时间字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String fileName = LocalDateTime.now().format(formatter) + "_保单信息表.xlsx";
        File excelFile = new File("D:/Test/" + fileName);
        // 检查Excel文件是否存在，如果不存在则尝试创建它
        if (!excelFile.exists()) {
            try {
                excelFile.createNewFile();
            } catch (IOException e) {
                // 如果文件创建过程中发生IO异常，打印异常信息
                e.printStackTrace();
            }
        }

        List<ExcelExporter> excelExporterList = new ArrayList<>();

        // 从数据库获取指定数量的保险政策信息列表
        List<InsurancePolicy> insurancePolicyList = insurancePolicyMapper.getInsurancePolicyList(count);

        // 遍历保险政策列表，为每个政策创建一个ExcelExporter对象，并添加到列表中
        for (InsurancePolicy insurancePolicy : insurancePolicyList) {

            ExcelExporter excelExporter = BeanUtil.toBean(insurancePolicy, ExcelExporter.class);

            // 获取与当前保险政策关联的客户信息列表，并根据客户类型分割为投保人、被保险人和受益人
            List<CustomerInfo> customerInfoList = customerInfoMapper.getCustomerInfoByPolicyNo(insurancePolicy.getPolicyNo());

            String policyholder = customerInfoList.stream().filter(customer -> "A".equals(customer.getCustomerType())).map(CustomerInfo::getFullName).collect(Collectors.toList()).toString().replaceAll("[\\[\\]]", "");
            String insured = customerInfoList.stream().filter(customer -> "B".equals(customer.getCustomerType())).map(CustomerInfo::getFullName).collect(Collectors.toList()).toString().replaceAll("[\\[\\]]", "");
            String beneficiary = customerInfoList.stream().filter(customer -> "S".equals(customer.getCustomerType())).map(CustomerInfo::getFullName).collect(Collectors.toList()).toString().replaceAll("[\\[\\]]", "");

            //属性设置
            excelExporter.setPolicyholder(policyholder);
            excelExporter.setInsured(insured);
            excelExporter.setBeneficiary(beneficiary);

            String policyStatus = enumValue.getEnumByCode(excelExporter.getPolicyStatus());
            if(BeanUtil.isNotEmpty(policyStatus)){
                excelExporter.setPolicyStatus(policyStatus);
            }

            String beneficiaryType = enumValue.getEnumByCode(excelExporter.getBeneficiaryType());
            if(BeanUtil.isNotEmpty(beneficiaryType)){
                excelExporter.setBeneficiaryType(beneficiaryType);
            }

            String paymentMethod = enumValue.getEnumByCode(excelExporter.getPaymentMethod());
            if(BeanUtil.isNotEmpty(paymentMethod)){
                excelExporter.setPaymentMethod(paymentMethod);
            }

            //添加到打印列表里
            excelExporterList.add(excelExporter);
        }

        // 使用EasyExcel库将保险政策信息列表写入Excel文件中
        // 这里指定Excel文件、数据模型类、工作表名称和要写入的数据列表
        EasyExcel.write(excelFile, ExcelExporter.class).sheet("保单信息").doWrite(excelExporterList);
    }
}