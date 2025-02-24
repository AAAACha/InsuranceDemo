package com.schj.mapper;

import com.github.pagehelper.Page;
import com.schj.pojo.dto.request.PolicyQueryRequest;
import com.schj.pojo.po.InsurancePolicy;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author AvA
 * @since 2025-02-21
 */
@Mapper
public interface InsurancePolicyMapper{

    @Select("select count(*) from insurance_system.insurance_policy ip where id = #{id}")
    int getInsurancePolicyCount(Long id);

    @Update("update insurance_policy set is_deleted = 1 where id = #{id}")
    void deleteInsurancePolicyById(Long id);

    //@Select("select * from insurance_system.insurance_policy ip where id = #{id}")
    InsurancePolicy getInsurancePolicyById(Long id);

    List<InsurancePolicy> getInsurancePolicyList(int count);

    Page list(PolicyQueryRequest policyQueryRequest);

    void insertInsurancePolicy(InsurancePolicy insurancePolicy);

    @Delete("delete from insurance_policy where id = #{id}")
    void deleteInsurancePolicy(Long id);

    void updateInsurancePolicyById(InsurancePolicy insurancePolicy);
}
