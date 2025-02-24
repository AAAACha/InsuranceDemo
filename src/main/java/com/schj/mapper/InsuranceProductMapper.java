package com.schj.mapper;


import com.schj.pojo.po.InsuranceProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}
