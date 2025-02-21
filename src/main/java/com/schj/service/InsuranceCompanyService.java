package com.schj.service;

import com.schj.pojo.dto.request.InsuranceCompanyReqDTO;
import com.schj.pojo.dto.response.InsuranceCompanyResDTO;
import org.springframework.stereotype.Service;

/**
 * @author AvA
 * @since 2025-02-21
 */
@Service
public interface InsuranceCompanyService{

    Boolean insertInsuranceCompany(InsuranceCompanyReqDTO insuranceCompanyReqDTO);

    InsuranceCompanyResDTO getInsuranceCompanyById(Long id);

    Boolean updateInsuranceCompanyById(Long id, InsuranceCompanyReqDTO insuranceCompanyReqDTO);

    Boolean deleteInsuranceCompanyById(Long id);
}
