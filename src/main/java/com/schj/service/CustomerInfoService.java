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

    Boolean insertCustomerInfoService(CustomerInfoReqDTO customerInfoReqDTO);

    CustomerInfoResDTO getCustomerInfoById(Long id);

    Boolean updateCustomerInfoById(Long id, CustomerInfoReqDTO customerInfoReqDTO);

    Boolean deleteCustomerInfoById(Long id);
}
