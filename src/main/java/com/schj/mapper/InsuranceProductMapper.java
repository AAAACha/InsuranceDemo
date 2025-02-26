package com.schj.mapper;


import com.schj.pojo.po.InsuranceProduct;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author AvA
 * @since 2025-02-21
 */
@Mapper
public interface InsuranceProductMapper{

    Integer insertInsuranceProduct(InsuranceProduct insuranceProduct);

    InsuranceProduct getInsuranceProductById(Long id);

    Integer updateInsuranceProductById(InsuranceProduct insuranceProduct);

    Integer deleteInsuranceProductById(Long id);

    List<InsuranceProduct> selectInsuranceProductByIdList(List<Long> productIdList);

    /**
     * 根据险种代码 缴费年限 缴费方式 查询险种
     * @param productList
     * @return
     */
    Integer getInsuranceProductByCodeAndYearsAndMethod(List<InsuranceProduct> productList);
}
