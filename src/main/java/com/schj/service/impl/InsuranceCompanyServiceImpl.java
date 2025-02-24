package com.schj.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.schj.mapper.EnumValue;
import com.schj.mapper.InsuranceCompanyMapper;
import com.schj.pojo.dto.request.InsuranceCompanyReqDTO;
import com.schj.pojo.dto.response.InsuranceCompanyResDTO;
import com.schj.pojo.po.InsuranceCompany;
import com.schj.service.InsuranceCompanyService;
import com.schj.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @Descriptioin InsuranceCompanyServiceImpl
 * @Author AvA
 * @Date 2025-02-21
 */
@Service
public class InsuranceCompanyServiceImpl implements InsuranceCompanyService {

    @Autowired
    InsuranceCompanyMapper insuranceCompanyMapper;

    @Autowired
    EnumValue enumValue;

    SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(1);

    /**
     * 插入新的保险公司记录
     *
     * @param insuranceCompanyReqDTO 包含要插入的保险公司信息的请求数据传输对象
     */
    @Override
    public void insertInsuranceCompany(InsuranceCompanyReqDTO insuranceCompanyReqDTO) {

        InsuranceCompany insuranceCompany = new InsuranceCompany();

        // 生成唯一的保险公司ID
        long id = snowflakeIdWorker.nextId();

        insuranceCompany.setId(id);
        insuranceCompany.setCompanyCode(insuranceCompanyReqDTO.getCompanyCode());
        insuranceCompany.setCompanyName(insuranceCompanyReqDTO.getCompanyName());

        // 根据公司类型代码获取公司类型描述
        String CompanyType = enumValue.getEnumByCode(insuranceCompanyReqDTO.getCompanyType());
        if (BeanUtil.isNotEmpty(CompanyType)) {
            insuranceCompany.setCompanyType(CompanyType);
        } else {
            throw new RuntimeException("您输入的公司类型不存在");
        }

        // 设置创建者和创建时间等公共字段
        insuranceCompany.setCreator("admin");
        insuranceCompany.setCreatedTime(LocalDateTime.now());
        insuranceCompany.setUpdater("admin");
        insuranceCompany.setUpdatedTime(LocalDateTime.now());
        insuranceCompany.setIsDeleted(0);

        // 调用Mapper方法插入保险公司记录
        insuranceCompanyMapper.insertInsuranceCompany(insuranceCompany);
    }

    /**
     * 根据ID获取保险公司信息
     *
     * @param id 保险公司的唯一标识
     * @return 包含保险公司信息的响应数据传输对象
     */
    @Override
    public InsuranceCompanyResDTO getInsuranceCompanyById(Long id) {

        // 通过Mapper方法获取保险公司记录
        InsuranceCompany insuranceCompany = insuranceCompanyMapper.getInsuranceCompanyById(id);

        // 将保险公司记录转换为响应数据传输对象
        InsuranceCompanyResDTO result = BeanUtil.toBean(insuranceCompany, InsuranceCompanyResDTO.class);

        return result;
    }

    /**
     * 根据ID更新保险公司信息
     *
     * @param id 保险公司的唯一标识
     * @param insuranceCompanyReqDTO 包含更新后的保险公司信息的请求数据传输对象
     */
    @Override
    public void updateInsuranceCompanyById(Long id, InsuranceCompanyReqDTO insuranceCompanyReqDTO) {

        // 将请求数据传输对象转换为保险公司记录
        InsuranceCompany insuranceCompany = BeanUtil.toBean(insuranceCompanyReqDTO, InsuranceCompany.class);

        insuranceCompany.setId(id);
        insuranceCompany.setUpdater("admin");
        insuranceCompany.setUpdatedTime(LocalDateTime.now());

        // 更新公司类型描述
        String CompanyType = enumValue.getEnumByCode(insuranceCompanyReqDTO.getCompanyType());
        if (BeanUtil.isNotEmpty(CompanyType)) {
            insuranceCompany.setCompanyType(CompanyType);
        } else {
            throw new RuntimeException("您输入的公司类型不存在");
        }

        // 调用Mapper方法更新保险公司记录
        insuranceCompanyMapper.updateInsuranceCompanyById(insuranceCompany);
    }

    /**
     * 根据ID删除保险公司记录
     *
     * @param id 保险公司的唯一标识
     */
    @Override
    public void deleteInsuranceCompanyById(Long id) {
        // 调用Mapper方法删除保险公司记录
        insuranceCompanyMapper.deleteInsuranceCompanyById(id);
    }
}
