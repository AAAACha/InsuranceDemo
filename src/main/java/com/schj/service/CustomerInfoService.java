package com.schj.service;

import com.schj.pojo.dto.request.CustomerInfoReqDTO;
import com.schj.pojo.dto.response.CustomerInfoResDTO;
import org.springframework.stereotype.Service;

/**
 * @author AvA
 * @since 2025-02-21
 */
@Service
public interface CustomerInfoService {

    void insertCustomerInfoService(CustomerInfoReqDTO customerInfoReqDTO);

    CustomerInfoResDTO getCustomerInfoById(Long id);

    void updateCustomerInfoById(Long id, CustomerInfoReqDTO customerInfoReqDTO);

    void deleteCustomerInfoById(Long id);
}
