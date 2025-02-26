package com.schj.mapper;


import com.schj.pojo.po.InsuranceProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author AvA
 * @since 2025-02-21
 */
@Mapper
public interface InsuranceProductMapper{

    Boolean insertInsuranceProduct(InsuranceProduct insuranceProduct);

    InsuranceProduct getInsuranceProductById(Long id);

    Boolean updateInsuranceProductById(InsuranceProduct insuranceProduct);

    Boolean deleteInsuranceProductById(Long id);

    @Select("select product_name from insurance_product where id = #{id}")
    String getInsuranceProductNameById(Long id);

    List<InsuranceProduct> selectInsuranceProductByIdList(List<Long> productIdList);

    /**
     * 根据险种代码 缴费年限 缴费方式 查询险种
     * @param productList
     * @return
     */
    int getInsuranceProductByCodeAndYearsAndMethod(List<InsuranceProduct> productList);
}
