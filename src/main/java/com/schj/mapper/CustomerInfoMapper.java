package com.schj.mapper;

import com.schj.pojo.po.CustomerInfo;
import org.apache.ibatis.annotations.Mapper;

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
}
