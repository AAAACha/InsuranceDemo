package com.schj.service.impl;


import cn.hutool.core.bean.BeanUtil;
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
    InsuranceProductMapper insuranceProductMapper;

    @Override
    public Boolean insertInsuranceProduct(InsuranceProductReqDTO insuranceProductReqDTO) {

        InsuranceProduct insuranceProduct = BeanUtil.toBean(insuranceProductReqDTO, InsuranceProduct.class);

        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(1);
        long id = snowflakeIdWorker.nextId();
        insuranceProduct.setId(id);

        insuranceProduct.setCreator("admin");
        insuranceProduct.setCreatedTime(LocalDateTime.now());
        insuranceProduct.setUpdater("admin");
        insuranceProduct.setUpdatedTime(LocalDateTime.now());
        insuranceProduct.setIsDeleted(0);

        return insuranceProductMapper.insertInsuranceProduct(insuranceProduct);
    }

    @Override
    public InsuranceProductResDTO getInsuranceProductById(Long id) {
        InsuranceProduct insuranceProduct =  insuranceProductMapper.getInsuranceProductById(id);

        InsuranceProductResDTO insuranceProductResDTO = BeanUtil.toBean(insuranceProduct, InsuranceProductResDTO.class);

        return insuranceProductResDTO;
    }

    @Override
    public Boolean updateInsuranceProductById(InsuranceProductReqDTO insuranceProductReqDTO) {

        InsuranceProduct insuranceProduct = BeanUtil.toBean(insuranceProductReqDTO, InsuranceProduct.class);

        insuranceProduct.setUpdater("admin");
        insuranceProduct.setUpdatedTime(LocalDateTime.now());

        return insuranceProductMapper.updateInsuranceProductById(insuranceProduct);
    }

    @Override
    public Boolean deleteInsuranceProductById(Long id) {
        return insuranceProductMapper.deleteInsuranceProductById(id);
    }
}
