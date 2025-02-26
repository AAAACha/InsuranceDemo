package com.schj.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.schj.mapper.EnumValue;
import com.schj.mapper.InsuranceProductMapper;
import com.schj.pojo.dto.request.InsuranceProductReqDTO;
import com.schj.pojo.dto.response.InsuranceProductResDTO;
import com.schj.pojo.po.InsuranceProduct;
import com.schj.pojo.po.Result;
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

    /**
     * 插入险种信息
     *
     * 该方法将接收到的险种请求数据转换为保险产品对象，并为其生成唯一的ID，
     * 同时验证主附类别、长短险类别和险种状态，若验证通过则设置创建者和创建时间等信息，
     * 最后调用Mapper层方法将保险险种插入数据库
     *
     * @param insuranceProductReqDTO 险种请求数据传输对象，包含待插入的险种信息
     */
    @Override
    public Result insertInsuranceProduct(InsuranceProductReqDTO insuranceProductReqDTO) {

        // 将请求数据转换为险种对象
        InsuranceProduct insuranceProduct = BeanUtil.toBean(insuranceProductReqDTO, InsuranceProduct.class);

        // 初始化ID生成器并为保险产品生成唯一ID
        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(1);
        long id = snowflakeIdWorker.nextId();
        insuranceProduct.setId(id);

        String resultMsg = checkProduct(insuranceProductReqDTO);
        if (BeanUtil.isNotEmpty(resultMsg)){
            return Result.error(resultMsg);
        }

        // 设置创建者和创建时间等信息
        insuranceProduct.setCreator("admin");
        insuranceProduct.setCreatedTime(LocalDateTime.now());
        insuranceProduct.setUpdater("admin");
        insuranceProduct.setUpdatedTime(LocalDateTime.now());
        insuranceProduct.setIsDeleted(0);

        // 调用Mapper层方法插入保险产品信息
        Integer affectedRows = insuranceProductMapper.insertInsuranceProduct(insuranceProduct);
        if(affectedRows > 0){
            return Result.success();
        } else {
            return Result.error("新增客户信息失败,请重试");
        }
    }

    /**
     * 根据ID获取保险产品信息
     *
     * 该方法通过ID从数据库中查询险种信息，并将其转换为响应数据传输对象返回
     *
     * @param id 保险产品的唯一标识符
     * @return InsuranceProductResDTO 包含保险产品信息的响应数据传输对象
     */
    @Override
    public Result getInsuranceProductById(Long id) {
        // 通过ID从数据库中查询保险产品信息
        InsuranceProduct insuranceProduct =  insuranceProductMapper.getInsuranceProductById(id);

        // 将查询到的保险产品信息转换为响应数据传输对象
        InsuranceProductResDTO insuranceProductResDTO = BeanUtil.toBean(insuranceProduct, InsuranceProductResDTO.class);

        String productCategory = enumValue.getEnumByCode(insuranceProductResDTO.getProductCategory());
        if(BeanUtil.isNotEmpty(productCategory)){
            insuranceProductResDTO.setProductCategory(productCategory);
        } else {
            return Result.error("您查询的险种的主附险类型不合法");
        }

        String durationType = enumValue.getEnumByCode(insuranceProductResDTO.getDurationType());
        if(BeanUtil.isNotEmpty(durationType)){
            insuranceProductResDTO.setDurationType(durationType);
        } else {
            return Result.error("您查询的险种的长短险类型不合法");
        }

        String paymentMethod = enumValue.getEnumByCode(insuranceProductResDTO.getPaymentMethod());
        if(BeanUtil.isNotEmpty(paymentMethod)){
            insuranceProductResDTO.setPaymentMethod(paymentMethod);
        } else {
            return Result.error("您查询的险种的缴费类型不合法");
        }

        String productStatus = enumValue.getEnumByCode(insuranceProductResDTO.getProductStatus());
        if(BeanUtil.isNotEmpty(productStatus)){
            insuranceProductResDTO.setProductStatus(productStatus);
        } else {
            return Result.error("您查询的险种的状态不合法");
        }

        if(BeanUtil.isNotEmpty(insuranceProductResDTO)){
            // 返回保险产品信息的响应数据传输对象
            return Result.success(insuranceProductResDTO);
        } else {
            return Result.error("您查询的险种信息不存在");
        }
    }

    /**
     * 更新保险产品信息
     *
     * 该方法根据ID和接收到的险种请求数据更新数据库中的险种信息，包括主附险类别、
     * 长短险类型和险种状态的验证，最后更新修改者和修改时间等信息
     *
     * @param id 保险产品的唯一标识符
     * @param insuranceProductReqDTO 包含待更新的保险产品信息的请求数据传输对象
     */
    @Override
    public Result updateInsuranceProductById(Long id, InsuranceProductReqDTO insuranceProductReqDTO) {

        // 将请求数据转换为保险产品对象
        InsuranceProduct insuranceProduct = BeanUtil.toBean(insuranceProductReqDTO, InsuranceProduct.class);

        // 设置保险产品的ID
        insuranceProduct.setId(id);

        String resultMsg = checkProduct(insuranceProductReqDTO);
        if (BeanUtil.isNotEmpty(resultMsg)){
            return Result.error(resultMsg);
        }

        // 设置修改者和修改时间等信息
        insuranceProduct.setUpdater("admin");
        insuranceProduct.setUpdatedTime(LocalDateTime.now());

        // 调用Mapper层方法更新保险产品信息
        Integer affectedRows = insuranceProductMapper.updateInsuranceProductById(insuranceProduct);
        if(affectedRows > 0){
            return Result.success();
        } else {
            return Result.error("修改客户信息失败,请重试");
        }
    }

    /**
     * 根据ID删除保险产品信息
     *
     * 该方法通过ID从数据库中删除保险产品信息
     *
     * @param id 保险产品的唯一标识符
     */
    @Override
    public Result deleteInsuranceProductById(Long id) {
        // 调用Mapper层方法根据ID删除保险产品信息
        Integer affectedRows = insuranceProductMapper.deleteInsuranceProductById(id);
        if(affectedRows > 0){
            return Result.success();
        } else {
            return Result.error("新增客户信息失败,请重试");
        }
    }

    /**
     * 判断险种信息
     * @param insuranceProductReqDTO
     */
    private String checkProduct(InsuranceProductReqDTO insuranceProductReqDTO){

        String resultMsg = "";

        // 验证并设置主附类别
        String productCategory = enumValue.getEnumByCode(insuranceProductReqDTO.getProductCategory());
        if(BeanUtil.isEmpty(productCategory)){
            resultMsg = "您输入的主附险标识不存在";
            return resultMsg;
        }

        // 验证并设置长短险类型
        String durationType = enumValue.getEnumByCode(insuranceProductReqDTO.getDurationType());
        if(BeanUtil.isEmpty(durationType)){
            resultMsg = "您输入的长短险标识不存在";
            return resultMsg;
        }

        // 验证并设置险种状态
        String productStatus = enumValue.getEnumByCode(insuranceProductReqDTO.getProductStatus());
        if(BeanUtil.isEmpty(productStatus)){
            resultMsg = "您输入的险种状态不存在";
            return resultMsg;
        }

        return resultMsg;
    }
}