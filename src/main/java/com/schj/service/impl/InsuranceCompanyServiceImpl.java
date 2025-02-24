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

    @Override
    public void insertInsuranceCompany(InsuranceCompanyReqDTO insuranceCompanyReqDTO) {

        InsuranceCompany insuranceCompany = new InsuranceCompany();

        long id = snowflakeIdWorker.nextId();

        insuranceCompany.setId(id);
        insuranceCompany.setCompanyCode(insuranceCompanyReqDTO.getCompanyCode());
        insuranceCompany.setCompanyName(insuranceCompanyReqDTO.getCompanyName());

        String CompanyType = enumValue.getEnumByCode(insuranceCompanyReqDTO.getCompanyType());
        if (BeanUtil.isNotEmpty(CompanyType)) {
            insuranceCompany.setCompanyType(CompanyType);
        } else {
            throw new RuntimeException("您输入的公司类型不存在");
        }

        insuranceCompany.setCreator("admin");
        insuranceCompany.setCreatedTime(LocalDateTime.now());
        insuranceCompany.setUpdater("admin");
        insuranceCompany.setUpdatedTime(LocalDateTime.now());
        insuranceCompany.setIsDeleted(0);

        insuranceCompanyMapper.insertInsuranceCompany(insuranceCompany);
    }

    @Override
    public InsuranceCompanyResDTO getInsuranceCompanyById(Long id) {

        InsuranceCompany insuranceCompany = insuranceCompanyMapper.getInsuranceCompanyById(id);

        InsuranceCompanyResDTO result = BeanUtil.toBean(insuranceCompany, InsuranceCompanyResDTO.class);

        return result;
    }

    @Override
    public void updateInsuranceCompanyById(Long id, InsuranceCompanyReqDTO insuranceCompanyReqDTO) {

        InsuranceCompany insuranceCompany = BeanUtil.toBean(insuranceCompanyReqDTO, InsuranceCompany.class);

        insuranceCompany.setId(id);
        insuranceCompany.setUpdater("admin");
        insuranceCompany.setUpdatedTime(LocalDateTime.now());

        String CompanyType = enumValue.getEnumByCode(insuranceCompanyReqDTO.getCompanyType());
        if (BeanUtil.isNotEmpty(CompanyType)) {
            insuranceCompany.setCompanyType(CompanyType);
        } else {
            throw new RuntimeException("您输入的公司类型不存在");
        }

        insuranceCompanyMapper.updateInsuranceCompanyById(insuranceCompany);
    }

    @Override
    public void deleteInsuranceCompanyById(Long id) {
        insuranceCompanyMapper.deleteInsuranceCompanyById(id);
    }
}
