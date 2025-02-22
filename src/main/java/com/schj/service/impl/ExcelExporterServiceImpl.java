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

    /**
     * 重写ExcelExporter方法，用于导出保险政策信息到Excel文件
     * 此方法根据指定的数量从数据库获取保险政策信息，并将其写入到Excel文件中
     * 如果目标Excel文件不存在，此方法将创建一个新的Excel文件
     *
     * @param count 指定从数据库获取保险政策信息的数量
     */
    @Override
    public void ExcelExporter(int count) {
        // 定义Excel文件的路径和名称
        File excelFile = new File("D:/Test/保单信息表.xlsx");
        // 检查Excel文件是否存在，如果不存在则尝试创建它
        if (!excelFile.exists()) {
            try {
                excelFile.createNewFile();
            } catch (IOException e) {
                // 如果文件创建过程中发生IO异常，打印异常信息
                e.printStackTrace();
            }
        }

        // 从数据库获取指定数量的保险政策信息列表
        List<InsurancePolicy> list = insurancePolicyMapper.getInsurancePolicyList(count);

        // 使用EasyExcel库将保险政策信息列表写入Excel文件中
        // 这里指定Excel文件、数据模型类、工作表名称和要写入的数据列表
        EasyExcel.write(excelFile, InsurancePolicy.class).sheet("保单信息").doWrite(list);
    }

}
