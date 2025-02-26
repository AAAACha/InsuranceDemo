package com.schj.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.schj.mapper.CustomerInfoMapper;
import com.schj.mapper.EnumValue;
import com.schj.pojo.dto.request.CustomerInfoReqDTO;
import com.schj.pojo.dto.response.CustomerInfoResDTO;
import com.schj.pojo.po.CustomerInfo;
import com.schj.pojo.po.Result;
import com.schj.service.CustomerInfoService;
import com.schj.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @Descriptioin CustomerInfoServiceImpl
 * @Author AvA
 * @Date 2025-02-21
 */
@Service
public class CustomerInfoServiceImpl implements CustomerInfoService {

    @Autowired
    private CustomerInfoMapper customerInfoMapper;

    @Autowired
    private EnumValue enumValue;

    SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(1);

    /**
     * 插入客户信息的service方法
     *
     * @param customerInfoReqDTO 客户信息请求DTO，包含要插入的客户信息
     */
    @Override
    public Result insertCustomerInfoService(CustomerInfoReqDTO customerInfoReqDTO) {

        // 将客户信息请求DTO转换为客户信息对象
        CustomerInfo customerInfo = BeanUtil.toBean(customerInfoReqDTO, CustomerInfo.class);

        // 生成唯一的ID
        long id = snowflakeIdWorker.nextId();

        // 设置客户信息的ID
        customerInfo.setId(id);

        //检查证件类型是否合规
        if(BeanUtil.isNotEmpty(checkIdType(customerInfoReqDTO))){
            return Result.error("您输入的证件类型不合法,请输入正确的证件类型");
        }

        // 设置创建时间和创建者
        customerInfo.setCreatedTime(LocalDateTime.now());
        customerInfo.setCreator("admin");
        // 设置更新时间和更新者
        customerInfo.setUpdatedTime(LocalDateTime.now());
        customerInfo.setUpdater("admin");
        // 设置删除标志为未删除
        customerInfo.setIsDeleted(0);

        // 调用mapper方法插入客户信息，并返回操作结果
        Integer affectedRows = customerInfoMapper.insertCustomerInfoService(customerInfo);
        if(affectedRows > 0){
            return Result.success();
        } else {
            return Result.error("新增客户信息失败,请重试");
        }
    }

    /**
     * 根据ID获取客户信息的service方法
     *
     * @param id 客户信息的唯一标识
     * @return 返回客户信息的响应DTO，包含查询到的客户信息
     */
    @Override
    public Result getCustomerInfoById(Long id) {

        // 根据ID从数据库中查询客户信息
        CustomerInfo customerInfo = customerInfoMapper.getCustomerInfoById(id);

        // 将查询到的客户信息转换为响应DTO
        CustomerInfoResDTO customerInfoResDTO = BeanUtil.toBean(customerInfo, CustomerInfoResDTO.class);

        String idType = enumValue.getEnumByCode(customerInfoResDTO.getIdType());
        if(BeanUtil.isNotEmpty(idType)){
            customerInfoResDTO.setIdType(idType);
        } else {
            return Result.error("您查询的用户的证件类型不合法");
        }

        if(BeanUtil.isNotEmpty(customerInfoResDTO)){
            // 返回客户信息的响应DTO
            return Result.success(customerInfoResDTO);
        } else {
            return Result.error("您查询的用户信息不存在");
        }
    }

    /**
     * 根据ID更新客户信息的service方法
     *
     * @param id 客户信息的唯一标识
     * @param customerInfoReqDTO 客户信息请求DTO，包含要更新的客户信息
     */
    @Override
    public Result updateCustomerInfoById(Long id, CustomerInfoReqDTO customerInfoReqDTO) {

        // 属性拷贝，将请求DTO转换为客户信息对象
        CustomerInfo customerInfo = BeanUtil.toBean(customerInfoReqDTO, CustomerInfo.class);

        //检查证件类型是否合规
        if(BeanUtil.isNotEmpty(checkIdType(customerInfoReqDTO))){
            return Result.error("您输入的证件类型不合法,请输入正确的证件类型");
        }

        // 字段填充，设置ID、更新者和更新时间
        customerInfo.setId(id);
        customerInfo.setUpdater("admin");
        customerInfo.setUpdatedTime(LocalDateTime.now());

        // 调用mapper方法更新客户信息
        Integer affectedRows = customerInfoMapper.updateCustomerInfoById(customerInfo);
        if(affectedRows > 0){
            return Result.success();
        } else {
            return Result.error("修改客户信息失败,请重试");
        }
    }

    /**
     * 根据ID删除客户信息的service方法
     *
     * @param id 客户信息的唯一标识
     */
    @Override
    public Result deleteCustomerInfoById(Long id) {
        // 调用mapper方法删除客户信息
        Integer affectedRows= customerInfoMapper.deleteCustomerInfoById(id);
        if(affectedRows > 0){
            return Result.success();
        } else {
            return Result.error("您要删除的客户信息不存在");
        }
    }

    /**
     * 检查证件类型是否合规
     * @param customerInfoReqDTO
     */
    private String checkIdType(CustomerInfoReqDTO customerInfoReqDTO){
        String resultMsg = "";

        // 根据代码获取证件类型枚举值
        String idType = enumValue.getEnumByCode(customerInfoReqDTO.getIdType());
        //判断证件类型是否存在
        if (BeanUtil.isEmpty(idType)) {
            resultMsg = "您输入的证件类型不合法,请输入正确的证件类型";
            return resultMsg;
        }
        return resultMsg;
    }
}
