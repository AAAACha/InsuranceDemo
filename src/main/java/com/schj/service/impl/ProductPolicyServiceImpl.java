package com.schj.service.impl;

import com.schj.pojo.po.ProductPolicy;
import com.schj.service.ProductPolicyService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Descriptioin ProductPolicyServiceImpl
 * @Author AvA
 * @Date 2025-02-22
 */
@Service
public class ProductPolicyServiceImpl implements ProductPolicyService {

    @Override
    public void insertProductPolicy(ProductPolicy productPolicy) {

    }

    @Override
    public List<ProductPolicy> getProductPolicyByPolicyId(Long id) {
        return List.of();
    }

    @Override
    public void updateProductPolicy(ProductPolicy productPolicy) {

    }

    @Override
    public void deleteProductPolicyByPolicyId(Long id) {

    }

    @Override
    public List<Long> getProductIdListByPolicyId(Long id) {
        return List.of();
    }
}
