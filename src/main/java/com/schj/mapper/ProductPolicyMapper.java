package com.schj.mapper;

import com.schj.pojo.po.ProductPolicy;
import org.apache.ibatis.annotations.Mapper;

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
}
