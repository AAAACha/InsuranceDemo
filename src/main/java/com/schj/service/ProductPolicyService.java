package com.schj.service;

import com.schj.pojo.po.ProductPolicy;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Descriptioin ProductPolicyService
 * @Author AvA
 * @Date 2025-02-22
 */
@Service
public interface ProductPolicyService {

    // 插入新的 ProductPolicy 记录
    void insertProductPolicy(ProductPolicy productPolicy);

    // 根据 PolicyIID 获取 ProductPolicy 记录
    List<ProductPolicy> getProductPolicyByPolicyId(Long id);

    // 更新 ProductPolicy 记录
    void updateProductPolicy(ProductPolicy productPolicy);

    // 根据 PolicyID 删除 ProductPolicy 记录
    void deleteProductPolicyByPolicyId(Long id);

    List<Long> getProductIdListByPolicyId(Long id);
}
