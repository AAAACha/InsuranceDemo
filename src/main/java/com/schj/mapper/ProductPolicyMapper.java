package com.schj.mapper;

import com.schj.pojo.po.ProductPolicy;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Descriptioin ProductPolicyMapper
 * @Author AvA
 * @Date 2025-02-24
 */
@Mapper
public interface ProductPolicyMapper {
    /**
     * 插入产品保单信息
     * @param productPolicy
     */
    void insertProductPolicy(ProductPolicy productPolicy);

    /**
     * 根据Policyid删除产品保单信息
     * @param policyId
     */
    @Delete("delete from product_policy where policy_id = #{policyId}")
    void deleteProductPolicyByPolicyId(Long policyId);

    /**
     * 根据PolicyId查询productId集合
     * @param id
     * @return
     */
    @Select("select product_id from product_policy where policy_id = #{id}")
    List<Long> getProductIdListByPolicyId(Long id);
}
