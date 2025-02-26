package com.schj.mapper;

import com.github.pagehelper.Page;
import com.schj.pojo.dto.request.PolicyQueryRequest;
import com.schj.pojo.po.InsurancePolicy;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author AvA
 * @since 2025-02-21
 */
@Mapper
public interface InsurancePolicyMapper{

    @Update("update insurance_policy set is_deleted = 1 where policy_no = #{policyNo}")
    void deleteInsurancePolicy(Long policy_no);

    //@Select("select * from insurance_system.insurance_policy ip where id = #{id}")
    InsurancePolicy getInsurancePolicyById(Long id);

    List<InsurancePolicy> getInsurancePolicyList(int count);

    Page list(PolicyQueryRequest policyQueryRequest);

    void insertInsurancePolicy(InsurancePolicy insurancePolicy);

    void updateInsurancePolicyById(InsurancePolicy insurancePolicy);

    @Delete("delete from insurance_policy where policy_no = #{policyNo}")
    void deleteInsurancePolicyByPolicyNo(Long policyNo);

    void batchInsertInsurancePolicy(List<InsurancePolicy> policyList);
}
