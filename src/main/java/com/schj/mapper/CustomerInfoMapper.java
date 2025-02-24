package com.schj.mapper;

import com.schj.pojo.po.CustomerInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author AvA
 * @since 2025-02-21
 */
@Mapper
public interface CustomerInfoMapper{

    /**
     * 新增客户信息
     * @param customerInfo
     * @return
     */
    Boolean insertCustomerInfoService(CustomerInfo customerInfo);

    /**
     * 根据id查询客户信息
     * @param id
     * @return
     */
    CustomerInfo getCustomerInfoById(Long id);

    /**
     * 根据id修改客户信息
     * @param customerInfo
     * @return
     */
    Boolean updateCustomerInfoById(CustomerInfo customerInfo);

    /**
     * 根据id删除客户信息
     * @param id
     * @return
     */
    Boolean deleteCustomerInfoById(Long id);

    /**
     * 根据保单id删除客户信息
     * @param id
     */
    void deleteInsurancePolicyByPolicyId(Long id);

    /**
     * 根据保单id查找客户信息
     * @param id
     * @return
     */
    List<Long> getCustomerInfoByPolicyId(Long id);

    @Delete("delete from customer_info where policy_id = #{policyId}")
    void deleteCustomerInfo(Long policyId);
}
