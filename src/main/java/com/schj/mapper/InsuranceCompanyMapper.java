package com.schj.mapper;

import com.schj.pojo.po.InsuranceCompany;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author AvA
 * @since 2025-02-21
 */
@Mapper
public interface InsuranceCompanyMapper{

    Boolean insertInsuranceCompany(InsuranceCompany insuranceCompany);

    InsuranceCompany getInsuranceCompanyById(Long id);

    Boolean updateInsuranceCompanyById(InsuranceCompany insuranceCompany);

    Boolean deleteInsuranceCompanyById(Long id);
}
