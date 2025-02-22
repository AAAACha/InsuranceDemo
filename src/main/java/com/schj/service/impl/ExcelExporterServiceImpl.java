package com.schj.service.impl;

import com.alibaba.excel.EasyExcel;
import com.schj.mapper.InsurancePolicyMapper;
import com.schj.pojo.po.InsurancePolicy;
import com.schj.service.ExcelExporterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Descriptioin ExcelExporterServiceImpl
 * @Author AvA
 * @Date 2025-02-22
 */
@Service
public class ExcelExporterServiceImpl implements ExcelExporterService {

    @Autowired
    private InsurancePolicyMapper insurancePolicyMapper;

    @Override
    public void ExcelExporter(int count) {

        File excelFile = new File("D:/Test/保单信息表.xlsx");
        if (!excelFile.exists()) {
            try {
                excelFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<InsurancePolicy> list = insurancePolicyMapper.getInsurancePolicyList(count);

        EasyExcel.write(excelFile, InsurancePolicy.class).sheet("保单信息").doWrite(list);
    }
}
