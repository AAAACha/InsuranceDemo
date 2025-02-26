package com.schj.service;

import com.schj.pojo.dto.request.InsuranceCompanyReqDTO;
import com.schj.pojo.po.Result;
import org.springframework.stereotype.Service;

/**
 * @author AvA
 * @since 2025-02-21
 */
@Service
public interface InsuranceCompanyService{

    Result insertInsuranceCompany(InsuranceCompanyReqDTO insuranceCompanyReqDTO);

    Result getInsuranceCompanyById(Long id);

    Result updateInsuranceCompanyById(Long id, InsuranceCompanyReqDTO insuranceCompanyReqDTO);

    Result deleteInsuranceCompanyById(Long id);
}
