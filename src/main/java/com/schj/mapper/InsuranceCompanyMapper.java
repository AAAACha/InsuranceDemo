package com.schj.mapper;

import com.schj.pojo.po.InsuranceCompany;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 根据保司代码及名称查询保司数量
     * @param companyName
     * @return
     */
    @Select("select count(*) from insurance_company where company_code = #{companyCode} and company_name = #{companyName}")
    int selectCompanyByCodeAndName(Integer companyCode, String companyName);
}
