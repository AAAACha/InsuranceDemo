package com.schj.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.schj.mapper.CustomerInfoMapper;
import com.schj.pojo.dto.request.CustomerInfoReqDTO;
import com.schj.pojo.dto.response.CustomerInfoResDTO;
import com.schj.pojo.po.CustomerInfo;
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

    SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(1);

    @Override
    public Boolean insertCustomerInfoService(CustomerInfoReqDTO customerInfoReqDTO) {

        // 将客户信息请求DTO转换为客户信息对象
        CustomerInfo customerInfo = BeanUtil.toBean(customerInfoReqDTO, CustomerInfo.class);

        // 生成唯一的ID
        long id = snowflakeIdWorker.nextId();

        // 设置客户信息的ID
        customerInfo.setId(id);

        // 设置创建时间和创建者
        customerInfo.setCreatedTime(LocalDateTime.now());
        customerInfo.setCreator("admin");
        // 设置更新时间和更新者
        customerInfo.setUpdatedTime(LocalDateTime.now());
        customerInfo.setUpdater("admin");
        // 设置删除标志为未删除
        customerInfo.setIsDeleted(0);

        // 调用映射器方法插入客户信息，并返回操作结果
        return customerInfoMapper.insertCustomerInfoService(customerInfo);
    }

    @Override
    public CustomerInfoResDTO getCustomerInfoById(Long id) {

        CustomerInfo customerInfo = customerInfoMapper.getCustomerInfoById(id);

        CustomerInfoResDTO customerInfoResDTO = BeanUtil.toBean(customerInfo, CustomerInfoResDTO.class);

        return customerInfoResDTO;
    }

    @Override
    public Boolean updateCustomerInfoById(Long id, CustomerInfoReqDTO customerInfoReqDTO) {

        //属性拷贝
        CustomerInfo customerInfo = BeanUtil.toBean(customerInfoReqDTO, CustomerInfo.class);

        //字段填充
        customerInfo.setId(id);
        customerInfo.setUpdater("admin");
        customerInfo.setUpdatedTime(LocalDateTime.now());

        //调用updateCustomerInfoById方法并返回
        return customerInfoMapper.updateCustomerInfoById(customerInfo);
    }

    @Override
    public Boolean deleteCustomerInfoById(Long id) {
        return customerInfoMapper.deleteCustomerInfoById(id);
    }
}
