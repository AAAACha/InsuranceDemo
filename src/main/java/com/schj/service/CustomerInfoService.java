package com.schj.service;

import com.schj.pojo.dto.request.CustomerInfoReqDTO;
import com.schj.pojo.po.Result;
import org.springframework.stereotype.Service;

/**
 * @author AvA
 * @since 2025-02-21
 */
@Service
public interface CustomerInfoService {

    Result insertCustomerInfoService(CustomerInfoReqDTO customerInfoReqDTO);

    Result getCustomerInfoById(Long id);

    Result updateCustomerInfoById(Long id, CustomerInfoReqDTO customerInfoReqDTO);

    Result deleteCustomerInfoById(Long id);
}
