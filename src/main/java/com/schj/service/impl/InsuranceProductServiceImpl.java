package com.schj.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.schj.mapper.EnumValue;
import com.schj.mapper.InsuranceProductMapper;
import com.schj.pojo.dto.request.InsuranceProductReqDTO;
import com.schj.pojo.dto.response.InsuranceProductResDTO;
import com.schj.pojo.po.InsuranceProduct;
import com.schj.service.InsuranceProductService;
import com.schj.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author AvA
 * @since 2025-02-21
 */
@Service
public class InsuranceProductServiceImpl implements InsuranceProductService {

    @Autowired
    private InsuranceProductMapper insuranceProductMapper;

    @Autowired
    private EnumValue enumValue;

    @Override
    public void insertInsuranceProduct(InsuranceProductReqDTO insuranceProductReqDTO) {

        InsuranceProduct insuranceProduct = BeanUtil.toBean(insuranceProductReqDTO, InsuranceProduct.class);

        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(1);
        long id = snowflakeIdWorker.nextId();
        insuranceProduct.setId(id);

        String productCategory = enumValue.getEnumByCode(insuranceProductReqDTO.getProductCategory());
        if(BeanUtil.isNotEmpty(productCategory)){
            insuranceProduct.setProductCategory(productCategory);
        } else {
            throw new RuntimeException("您输入的主附险标识不存在");
        }

        String durationType = enumValue.getEnumByCode(insuranceProductReqDTO.getDurationType());
        if(BeanUtil.isNotEmpty(durationType)){
            insuranceProduct.setDurationType(durationType);
        } else {
            throw new RuntimeException("您输入的长短险标识不存在");
        }

        String productStatus = enumValue.getEnumByCode(insuranceProductReqDTO.getProductStatus());
        if(BeanUtil.isNotEmpty(productStatus)){
            insuranceProduct.setProductStatus(productStatus);
        } else {
            throw new RuntimeException("您输入的险种状态不存在");
        }

        insuranceProduct.setCreator("admin");
        insuranceProduct.setCreatedTime(LocalDateTime.now());
        insuranceProduct.setUpdater("admin");
        insuranceProduct.setUpdatedTime(LocalDateTime.now());
        insuranceProduct.setIsDeleted(0);

        insuranceProductMapper.insertInsuranceProduct(insuranceProduct);
    }

    @Override
    public InsuranceProductResDTO getInsuranceProductById(Long id) {
        InsuranceProduct insuranceProduct =  insuranceProductMapper.getInsuranceProductById(id);

        InsuranceProductResDTO insuranceProductResDTO = BeanUtil.toBean(insuranceProduct, InsuranceProductResDTO.class);

        return insuranceProductResDTO;
    }

    @Override
    public void updateInsuranceProductById(Long id, InsuranceProductReqDTO insuranceProductReqDTO) {

        InsuranceProduct insuranceProduct = BeanUtil.toBean(insuranceProductReqDTO, InsuranceProduct.class);

        insuranceProduct.setId(id);

        String productCategory = enumValue.getEnumByCode(insuranceProductReqDTO.getProductCategory());
        if(BeanUtil.isNotEmpty(productCategory)){
            insuranceProduct.setProductCategory(productCategory);
        } else {
            throw new RuntimeException("您输入的主附险标识不存在");
        }

        String durationType = enumValue.getEnumByCode(insuranceProductReqDTO.getDurationType());
        if(BeanUtil.isNotEmpty(durationType)){
            insuranceProduct.setDurationType(durationType);
        } else {
            throw new RuntimeException("您输入的长短险标识不存在");
        }

        String productStatus = enumValue.getEnumByCode(insuranceProductReqDTO.getProductStatus());
        if(BeanUtil.isNotEmpty(productStatus)){
            insuranceProduct.setProductStatus(productStatus);
        } else {
            throw new RuntimeException("您输入的险种状态不存在");
        }

        insuranceProduct.setUpdater("admin");
        insuranceProduct.setUpdatedTime(LocalDateTime.now());

        insuranceProductMapper.updateInsuranceProductById(insuranceProduct);
    }

    @Override
    public void deleteInsuranceProductById(Long id) {
        insuranceProductMapper.deleteInsuranceProductById(id);
    }
}
