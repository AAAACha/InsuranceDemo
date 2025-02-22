package com.schj.mapper;

import com.schj.pojo.po.InsurancePolicy;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author AvA
 * @since 2025-02-21
 */
@Mapper
public interface InsurancePolicyMapper{

    @Select("select count(*) from insurance_system.insurance_policy ip where id = #{id}")
    int getInsurancePolicyCount(Long id);

    @Delete("delete from insurance_system.insurance_policy ip where id = #{id}")
    void deleteInsurancePolicyById(Long id);

    @Select("select * from insurance_system.insurance_policy ip where id = #{id}")
    InsurancePolicy getInsurancePolicyById(Long id);

    List<InsurancePolicy> getInsurancePolicyList(int count);
}
